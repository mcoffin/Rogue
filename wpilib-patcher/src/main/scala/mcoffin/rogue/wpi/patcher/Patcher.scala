package mcoffin.rogue.wpi.patcher

import java.io.File

import java.nio.file.{Files, Paths}

import java.net.URL
import java.net.URLClassLoader

import org.objectweb.asm.ClassReader
import org.objectweb.asm.ClassWriter
import org.objectweb.asm.tree.ClassNode

object Patcher extends App {
  val ROBOT_BASE_CLASS = "edu.wpi.first.wpilibj.RobotBase"

  val classNode = {
    def pathForClassName(name: String) = {
      val p = name.replace('.', '/')
      s"${p}.class"
    }

    val classLoader = {
      val jarURL = {
        val f = new File(args(0))
        f.toURI.toURL
      }
      println(s"Loading jar: ${jarURL}")
      new URLClassLoader(Array(jarURL))
    }

    val classPath = pathForClassName(ROBOT_BASE_CLASS)
    println(s"Loading class at path: ${classPath}")

    val classReader = new ClassReader(classLoader.getResourceAsStream(classPath))

    val cn = new ClassNode
    classReader.accept(cn, 0)
    cn
  }

  val classWriter = {
    val cw = new ClassWriter(ClassWriter.COMPUTE_MAXS | ClassWriter.COMPUTE_FRAMES)
    classNode.accept(cw)
    cw
  }

  private[Patcher] def writeOutput {
    val bytes = classWriter.toByteArray()
    Files.write(Paths.get("./RobotBase.class"), bytes)
  }

  writeOutput
}
