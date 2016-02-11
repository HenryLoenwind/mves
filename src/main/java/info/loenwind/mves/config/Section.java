package info.loenwind.mves.config;

import javax.annotation.Nonnull;

public enum Section {
  VISUALS("visuals", false),
  DEMO("demo"),
  DEV("development", false);

  @Nonnull
  public final String name;
  public final boolean sync;

  private Section(@Nonnull String name) {
    this.name = name;
    this.sync = true;
  }

  private Section(@Nonnull String name, boolean sync) {
    this.name = name;
    this.sync = sync;
  }

}
