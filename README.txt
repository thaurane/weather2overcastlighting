Weather2 Overcast Lighting 0.0.1
Minecraft 1.20.1 Forge

Purpose
-------
A separate client-side companion mod for Weather2 Morphologies.

It reads Weather2 layer 1 cloud objects and adds local overcast darkening without
modifying Weather2 source code.

Default behavior
----------------
- Samples layer 1 clouds around the player in a 128 block radius.
- Uses the existing 2.25x coverage correction.
- Darkening begins at 70% adjusted coverage.
- 70% coverage = 40% weather-style darkness.
- 100% coverage = 70% weather-style darkness.
- Smooths changes at 0.0025 per client tick.
- Applies the weather darkness only for the rendered frame.
- Restores Weather2/vanilla's actual thunder value afterward.
- Also darkens fog directly as a renderer-compatible fallback.
- Writes one [W2OL DEBUG] line every five seconds by default.

Weather2 is NOT modified by this project.

Building
--------
The project already contains a local Weather2 Morphologies jar in:
  libs/weather2-1.20.1-morphologies-dev.jar

That jar is used only as a compile/runtime dependency for development.
It is NOT bundled inside the finished companion mod jar.

Windows:
  gradlew.bat build

Output:
  build/libs/weather2-overcast-lighting-0.0.1.jar

Runtime
-------
Install:
- Weather2 Morphologies
- CoroUtil
- Weather2 Overcast Lighting

The companion mod is client-side. It does not need to be installed on a dedicated server.

Config
------
After first launch:
  config/weather2overcastlighting-client.toml

The defaults match the layer 1 darkness behavior previously tested inside Weather2.

Debug
-----
Search latest.log for:
  [W2OL DEBUG]

A startup line should also appear:
  [W2OL] Client event handlers registered on MinecraftForge.EVENT_BUS

If the startup line appears but no W2OL DEBUG lines appear while in a world, that
immediately tells us the runtime event subscription is the problem.
