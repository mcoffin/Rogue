package mcoffin.rogue.wpi.patcher

import java.io.BufferedOutputStream
import java.io.ByteArrayInputStream
import java.io.File
import java.io.FileOutputStream
import java.io.InputStream

import java.nio.file.{Files, Paths}

import java.net.URL
import java.net.URLClassLoader

import java.util.jar._

import org.objectweb.asm.ClassReader
import org.objectweb.asm.ClassWriter
import org.objectweb.asm.tree._

import scala.collection.JavaConversions._

object Patcher extends App {
  val ROBOT_BASE_CLASS = "edu.wpi.first.wpilibj.RobotBase"

  implicit class RobotBaseClassNode(val classNode: ClassNode) {
    lazy val constructorInstructions = {
      import org.objectweb.asm.Opcodes._

      val insnList = new InsnList
      val instructions = Seq(
        new VarInsnNode(ALOAD, 0),
        new MethodInsnNode(INVOKESPECIAL, "java/lang/Object", "<init>", "()V", false),
        new InsnNode(RETURN))
      instructions.foreach(insnList.add(_))
      insnList
    }

    def patchRobotBase {
      val methods = classNode.methods.map(m => m.asInstanceOf[MethodNode])
      val initMethods = methods.filter(m => m.name.equals("<init>"))
      initMethods.foreach(initMethod => {
        initMethod.instructions = constructorInstructions
      })
    }
  }

  private[Patcher] def jarFile = new File(args(0))

  def pathForClassName(name: String) = {
    val p = name.replace('.', '/')
    s"${p}.class"
  }
  val classPath = pathForClassName(ROBOT_BASE_CLASS)

  val classLoader = {
    val jarURL = {
      val f = jarFile
      f.toURI.toURL
    }
    println(s"Loading jar: ${jarURL}")
    new URLClassLoader(Array(jarURL))
  }

  private[Patcher] def classStream = classLoader.getResourceAsStream(classPath)

  val classNode = {
    println(s"Loading class at path: ${classPath}")

    val classReader = new ClassReader(classStream)

    val cn = new ClassNode
    classReader.accept(cn, 0)
    cn
  }

  classNode.patchRobotBase

  val classWriter = {
    val cw = new ClassWriter(ClassWriter.COMPUTE_MAXS | ClassWriter.COMPUTE_FRAMES)
    classNode.accept(cw)
    cw
  }

  private[Patcher] def writeOutput {
    val bytes = classWriter.toByteArray()
    val sourceJar = new JarFile(jarFile)
    val jarOutputStream = {
      val outputStream = new BufferedOutputStream(new FileOutputStream(args(1)))
      val jos = new JarOutputStream(outputStream)
      jos
    }

    def writeJarEntry(e: JarEntry, inputStream: InputStream) {
      jarOutputStream.putNextEntry(e)
      jarOutputStream.write(Stream.continually(inputStream.read()).takeWhile(_ != -1).map(_.toByte).toArray)
      jarOutputStream.closeEntry
    }

    try {
      sourceJar.entries.filter(e => !e.getName.equals(classPath)).foreach(e => writeJarEntry(e, sourceJar.getInputStream(e)))
      val robotBaseEntry = new JarEntry(classPath)
      writeJarEntry(robotBaseEntry, new ByteArrayInputStream(classWriter.toByteArray()))

      jarOutputStream.flush()
    } finally {
      jarOutputStream.close()
    }
  }

  writeOutput
}
