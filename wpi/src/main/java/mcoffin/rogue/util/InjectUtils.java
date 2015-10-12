package mcoffin.rogue.util;

import com.google.inject.Module;
import com.google.inject.util.Modules;

public class InjectUtils {
  public static Module createChainedOverrideModule(Module[] overridden, Module[] overrides) {
    return Modules.override(overridden).with(overrides);
  }
}
