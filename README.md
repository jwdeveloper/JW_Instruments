
> Thank you for using plugin! I believe it gives you much fun
> Be aware modifications of this file has NO impact on plugin
  To change plugin behaviour edit config.yml file

  - <B>[Information](#Information)</b>   General info about plugin and author 
  - <B>[Commands](#Commands)</b>      Command, arguments, command permissions
  - <B>[Permissions](#Permissions)</b>   Permissions and reactions between them
  - <B>[License](#License)</b>      Plugin license

Test

## Information                                                   
``` yaml
information:
  author: JW
  spigot-url: https://www.facebook.com/
  github-url: https://www.facebook.com/
  report-bug: https://www.facebook.com/

```

##  Commands                                                   
``` yaml
commands-tree:
    disable:
    instrument:
      lang:
      songs:
      get:
      resourcepack:
      update:


commands:
#============================= disable ==================================
    disable:
      description: disable all plugin without restarting server
      usage: Can be use only with Console

#================================================== instrument ========================================
    instrument:
      children:
        - lang
        - songs
        - get
        - resourcepack
        - update
      permissions:
        - instrument.commands.instrument
#================================================== lang ==============================================
    lang:
      permissions:
        - instrument.commands.lang
      arguments:
        - nationality:
            type: text
            description: change the language of plugin
            options:
                - en
                - kr
                - pl
      short-description: /instrument lang [en,pl,kr...]  (Change language of plugin)

#================================================== songs =============================================
    songs:
      permissions:
        - instrument.commands.songs

#================================================== get ===============================================
    get:
      permissions:
        - instrument.commands.get
      arguments:
        - guitar-type:
            type: custom
            description: select guitar type
            options:
                - acoustic
                - electric
                - classical

#================================================== resourcepack ======================================
    resourcepack:

#================================================== update ============================================
    update:
      permissions:
        - instrument.commands.update
```




##  Permissions                                                   
``` yaml
permissions:
#================================================== plugin ============================================
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

#================================================== commands ==========================================
    commands:
      description: Default permission for commands
      children:
        - instrument.commands.lang
        - instrument.commands.update
        - instrument.commands.instrument
        - instrument.commands.get
        - instrument.commands.songs

    instrument.commands.lang:
      description: /instrument lang [en,pl,kr...]  (Change language of plugin)
      default: op

    instrument.commands.update:
      description: /instrument update  (Updates plugin)
      default: op

    instrument.commands.instrument:
      description: /instrument (opens instrument gui)

    instrument.commands.get:
      description: /instrument get (pick your instrument)

    instrument.commands.songs:
      description: /instrument songs (opens songs gui)

#================================================== gui ===============================================
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

#================================================== gui [Instrument] ==================================
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

#================================================== gui [Songs] =======================================
    instrument.gui.songs:
      description: Songs GUI

    instrument.gui.songs.insert:
      description: Enable to insert new song

    instrument.gui.songs.edit:
      description: Enable to edit song

    instrument.gui.songs.delete:
      description: Enable to delete song
``` 
