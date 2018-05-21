# Railcraft - A Fork of the Minecraft Mod

Here you will find a fork of the Railcraft project, the main repo is linked below. Compiling instructions of the 1.12 branch apply to the official one, but can apply to this with very minor tweaking of the `git clone`.

## Official Links

* The Blog, Forums, and main download page: http://www.railcraft.info
* The Wiki: http://railcraft.info/wiki
* Main Github: https://github.com/Railcraft/Railcraft
* IRC: #railcraft on Esper.net - [WebChat](http://webchat.esper.net/?nick=RailcraftGithub...&channels=railcraft&prompt=1)
* Discord: [Invite](https://discord.gg/Wr9zxmP) - Linked with #railcraft on IRC
* CovertJaguar's Patreon: http://www.patreon.com/CovertJaguar


## Compiling for 1.12

The Railcraft Project follows standard [Forge](https://github.com/MinecraftForge/MinecraftForge) conventions for setting up and building a project, with a couple additional details. Like all Forge mods, the [Java 8 JDK](http://www.oracle.com/technetwork/java/javase/downloads/jdk8-downloads-2133151.html) will be needed and [Git](https://git-scm.com/downloads) if you don't want to manually download the necessary files for compiling.

The [API](https://github.com/CovertJaguar/Railcraft-API) and [Localization](https://github.com/CovertJaguar/Railcraft-Localization) files reside in their own repositories and are pulled automatically into the main repo as git submodules. These can be pulled in whilst cloning the code:
```
git clone -b liach-1.12.2 --single-branch --recursive https://github.com/Railcraft/Railcraft.git
```
Or manually downloaded and extracted into the same folder as the main project (if you've already gone and downloaded a zip of that).

### Running

To compile and run the game, navigate into the root folder of the project, then in the command line:
```
gradlew runClient
```
This process will take a long time the first time it is done in order to download all the necessary files Forge needs and to decompile Minecraft. Once it is complete any subsiquent runs will be much faster (as it only has to launch the game).

The game will run out of the `run` folder, thus any resource packs or config changes you might want will be done from in there. Any additional mods you might want will need to be deobfuscated versions, typically either `-dev` versions or normal ones processed using [BON](https://ci.tterrag.com/job/BON2).

If you want to log in (ie to have your skin whilst running), you can create a `gradle.properties` file in the root folder of the project with the following properties:
```
mcUsername=YourUserName
mcPassword=YourPassword
```

### Building

Before compiling you first have to promise first to yourself, then to everyone else that you're not going to distribute what you make. The Railcraft licence doesn't allow this, and you're only going to get in trouble if you break it. This means **no putting it in public modpacks**! If you want to use Railcraft on 1.12 you have to go through this process yourself. At least until CovertJaguar comes back to make a proper public release.

Now you've made your promise, navigate to `mods.railcraft.common.core.Railcraft` and delete the `fingerprintError(FMLFingerprintViolationEvent)` method. This is responsible for checking whether a jar is a valid *official* release or not, which what your making certainly isn't. You're still not planning to release this publicly right?

Finally to compile the code into a jar, navigate into the root folder of the project, then in the command line:
```
gradlew build
```
Once finished the produced jar will be in the `build/distributions` folder. **No sharing it!**


## Issues

Post bugs on the main repo [here](https://github.com/CovertJaguar/Railcraft/issues). Putting the crash report in a [Pastebin](https://pastebin.com/) first and mentioning it in the IRC or Discord is advisable for getting an opinion to whether it is a Railcraft bug or not. 
