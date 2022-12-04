> Thank you for using plugin! I believe it gives you much fun
> Documentation is divided to few sections
-   [Information]   General info about plugin and author
-   [Commands]      Command, arguments, command permissions
-   [Permissions]   Permissions and reactions between them
-   [License]       Plugin license
> Be aware modifications of this file has NO impact on plugin
> To change plugin behaviour edit config.yml file
## Information
``` yaml
information: 
  author: JW
  spigot-url: https://www.facebook.com/
  github-url: https://www.facebook.com/
  report-bug: https://www.facebook.com/

```
## Commands
``` yaml
commands-tree: 
disable: 
instrument: 
  resourcepack: 
  lang: 
  update: 
  songs: 
  get: 


commands: 
#disable
disable: 
    description: Command only for plugin development purpose. Can be only trigger by Console. disables all plugins
    usage: /disable

#instrument
instrument: 
    children: 
      - resourcepack
      - lang
      - update
      - songs
      - get
    permissions: 
      - instrument.commands.instrument
    description: opens instrument configuration GUI where player can modify behaviour currently using
    usage: /instrument or /instrument <children>
#resourcepack
resourcepack: 
    description: downloads plugin resourcepack
    usage: /instrument resourcepack

#lang
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

#update
update: 
    permissions: 
      - instrument.commands.update
    description: download plugin latest version, can be trigger both by player or console
    usage: /instrument update

#songs
songs: 
    permissions: 
      - instrument.commands.songs
    description: opens GUI where you can Edit, Create, Delete songs
    usage: /instrument songs

#get
get: 
    permissions: 
      - instrument.commands.get
    arguments: 
      - instrument-type:
          type: custom
          description: select instrument type
          options: 
              - electric
              - classical
              - acoustic
    description: by trigger this player will get selected instrument
    usage: /instrument get <instrument-type>



```
## Permissions
``` yaml
permissions: 

#plugin
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

#commands
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

#gui
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

#gui [Instrument]
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

#gui [Songs]
  instrument.gui.songs: 
    description: Songs GUI

  instrument.gui.songs.insert: 
    description: Enable to insert new song

  instrument.gui.songs.edit: 
    description: Enable to edit song

  instrument.gui.songs.delete: 
    description: Enable to delete song


```
