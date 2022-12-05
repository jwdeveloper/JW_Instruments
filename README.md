
![alt text](https://raw.githubusercontent.com/jwdeveloper/JW_Instruments/master/resources/plugin_baner.jpg)

<p align="center">
<a href="https://discord.gg/2hu6fPPeF7"><img src="https://raw.githubusercontent.com/jwdeveloper/SpigotFluentAPI/master/resources/social-media/discord.png"  /></a><a href="https://github.com/jwdeveloper/JW_Instruments"><img src="https://raw.githubusercontent.com/jwdeveloper/SpigotFluentAPI/master/resources/social-media/github.png"  /></a><a href="https://www.spigotmc.org/resources/instruments.106584/"><img src="https://raw.githubusercontent.com/jwdeveloper/SpigotFluentAPI/master/resources/social-media/spigot.png"  /></a></p>

If you are looking a plugin to enrich server experience this is the solution. Plugin adds bunch of instruments each of them is playable with all possible chords you can imagine. Therefore consider to use this plugin especially on RolePlay server

Plugin tutorial: https://www.youtube.com/watch?v=5X_T7s8Z6YQ&t=1s&ab_channel=JW

[![IMAGE ALT TEXT HERE](https://img.youtube.com/vi/5X_T7s8Z6YQ/0.jpg)](https://www.youtube.com/watch?v=5X_T7s8Z6YQ&t=1s&ab_channel=JW)

Test

![alt text](https://raw.githubusercontent.com/jwdeveloper/JW_Instruments/master/resources/guitars.gif)


![alt text](https://raw.githubusercontent.com/jwdeveloper/JW_Instruments/master/resources/chords.gif)


![alt text](https://raw.githubusercontent.com/jwdeveloper/JW_Instruments/master/resources/chordchange.gif)


![alt text](https://raw.githubusercontent.com/jwdeveloper/SpigotFluentAPI/master/resources/banners/configuration.png)

``` yaml
#
# plugin.language
# -> If you want add your language open `languages` folder copy `en.yml` call it as you want 
# set `language` property to your path name and /reload server 
#
# plugin.saving-frequency
# -> Determinate how frequent data is saved to files, value in minutes
#
#
# song.songs-limit
# -> Determine how much songs player can create 
# It's not applied for players with 
#  - op 
#  - instrument.song.no-limit 
#
#
# plugin.resourcepack.url
#    If you need to replace default resourcepack with your custom one
#    set this to link of you resourcepack
#    ! after plugin update make sure your custom resourcepack is compatible !
# plugin.resourcepack.load-on-join
#    Downloads resourcepack when player joins to server

plugin:
  version: ${version}
  language: en
  saving-frequency: 5
  resourcepack:
    url: https://github.com/jwdeveloper/JW_Instruments/releases/latest/download/instrumentpack.rar
    load-on-join: true
song:
  songs-limit: 5

```

![alt text](https://raw.githubusercontent.com/jwdeveloper/SpigotFluentAPI/master/resources/banners/commands.png)

``` yaml


commands: 
# /instrument or /instrument <children>
  instrument: 
    children: 
      - lang
      - resourcepack
      - songs
      - get
      - update
    permissions: 
      - instrument.commands.instrument
    description: opens instrument configuration GUI where player can modify behaviour currently using
    usage: /instrument or /instrument <children>
# /instrument lang <language>
  lang: 
    permissions: 
      - instrument.commands.lang
    arguments: 
      - language:
          type: text
          description: select language
          options: 
              - en
              - kr
              - pl
    description: Changes plugin languages, changes will be applied after server reload. Change be use both be player or console
    usage: /instrument lang <language>

# /instrument resourcepack
  resourcepack: 
    description: downloads plugin resourcepack
    usage: /instrument resourcepack

# /instrument songs
  songs: 
    permissions: 
      - instrument.commands.songs
    description: opens GUI where you can Edit, Create, Delete songs
    usage: /instrument songs

# /instrument get <instrument-type>
  get: 
    permissions: 
      - instrument.commands.get
    arguments: 
      - instrument-type:
          type: custom
          description: select instrument type
          options: 
              - classical
              - acoustic
              - electric
    description: by trigger this player will get selected instrument
    usage: /instrument get <instrument-type>

# /instrument update
  update: 
    permissions: 
      - instrument.commands.update
    description: download plugin latest version, can be trigger both by player or console
    usage: /instrument update



```

![alt text](https://raw.githubusercontent.com/jwdeveloper/SpigotFluentAPI/master/resources/banners/permissions.png)

``` yaml
permissions: 

# plugin
  instrument: 
    description: Default permission for plugin
    children: 
      - commands
      - gui
      - instrument.play
      - instrument.song.no-limit

  instrument.play: 
    description: Allows player to play the instrument

  instrument.song.no-limit: 
    description: Unlimited amount of songs player can create it also includes [song export]

# commands
  commands: 
    description: Default permission for commands
    children: 
      - instrument.commands.lang
      - instrument.commands.update
      - instrument.commands.instrument
      - instrument.commands.get
      - instrument.commands.songs

  instrument.commands.lang: 
    description: Allow player to change plugin language
    default: op

  instrument.commands.update: 
    description: players with this permission can update plugin
    default: op

  instrument.commands.instrument: 
    description: /instrument (opens instrument gui)

  instrument.commands.get: 
    description: /instrument get (pick your instrument)

  instrument.commands.songs: 
    description: /instrument songs (opens songs gui)

# gui
  gui: 
    description: Default permission for gui
    children: 
      - instrument.gui.instrument
      - instrument.gui.instrument.volume
      - instrument.gui.instrument.rhythm.change
      - instrument.gui.instrument.chords.display
      - instrument.gui.instrument.song.import
      - instrument.gui.instrument.song.export
      - instrument.gui.instrument.chords
      - instrument.gui.songs
      - instrument.gui.songs.insert
      - instrument.gui.songs.edit
      - instrument.gui.songs.delete

# gui [Instrument]
  instrument.gui.instrument: 
    description: Instrument gui

  instrument.gui.instrument.volume: 
    description: Change volume button

  instrument.gui.instrument.rhythm.change: 
    description: Change rhythm on shift press ON/OFF button

  instrument.gui.instrument.chords.display: 
    description: Display chords above inventory bar ON/OFF button

  instrument.gui.instrument.song.import: 
    description: Importing chords from a song

  instrument.gui.instrument.song.export: 
    description: Exporting chords to new song

  instrument.gui.instrument.chords: 
    description: Enable Editing chords in instrument GUI

# gui [Songs]
  instrument.gui.songs: 
    description: Songs GUI

  instrument.gui.songs.insert: 
    description: Enable to insert new song

  instrument.gui.songs.edit: 
    description: Enable to edit song

  instrument.gui.songs.delete: 
    description: Enable to delete song


```
