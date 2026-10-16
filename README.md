# BSM
Better storage mod is am od that serves as a replacement to ae2/rs2, sophisticated storage/backpacks, and other storage mods.

# Ideas
Instead of storage cells and disk as items, maybe it can be blocks, and i wanna add power managment to it where it can work as an energy distibutor to other systems.

## Dev run
1. From the repository root, run the development client in a terminal:

   - macOS/Linux: `./gradlew runClient`
   - Windows: `gradlew.bat runClient`

2. close the game
3. Move a energy mod like mekanism:https://www.curseforge.com/minecraft/mc-mods/mekanism/files/all?page=1&pageSize=20&showAlphaFiles=show to run/mods/
4. re run client

## Building

BSM requires Minecraft 1.21.1, Java 21, and NeoForge for Minecraft 1.21.1. This project is configured with NeoForge 21.1.256.

1. Install NeoForge for Minecraft 1.21.1 and launch the game once to create its instance folders.
2. Build the mod from the repository root:

   - macOS/Linux: `./gradlew build`
   - Windows: `gradlew.bat build`

(optional)
3. Copy `build/libs/bsm-1.0.0.jar` into the `mods` folder for the NeoForge instance you want to use. Create the folder if it does not exist. For the default Minecraft directory, it is `.minecraft/mods`; launcher-managed instances may have their own `mods` folder.
4. Launch Minecraft using the NeoForge profile.

# for reviewers in terra
to test the mod place an inifinite energy cube from mekanism or equivalent next to the block 