# blocks

A source code visualizing engine for Clojure.

Live at:  http://lincoln-b.com/blocks.

## Overview

This is a basic HTML/CSS engine for representing Clojure/ClojureScript semantic blocks as colored boxes. 

These are my goals:

1. The visual representation should be both more concise and more readable than the textual representation.
2. Any interactive/hover functionality should treat both the mouse and the keyboard as first-class citizens.
3. 

If you have suggestions or ideas to contribute, please send me a pull request or leave a comment on the ClojureVerse thread.

## Future

I see this repository as a collection of ideas for a tool that will one day become a fully-fledged Clojure IDE. Visual blocks by themselves aren't as interesting as the potential for additional tools that enable software developers to see their programs in new, powerful ways. 

For example, I can imagine a control flow diagram built into the editor, or a new way of organizing code that moves away from the linear file-based paradigm, or an easier and more powerful tool for refactoring code by dragging blocks across the screen. 

## Setup

To get an interactive development environment run:

    lein figwheel

and open your browser at [localhost:3449](http://localhost:3449/).
This will auto compile and send all changes to the browser without the
need to reload. After the compilation process is complete, you will
get a Browser Connected REPL. An easy way to try it is:

    (js/alert "Am I connected?")

and you should see an alert in the browser window.

To clean all compiled files:

    lein clean

To create a production build run:

    lein do clean, cljsbuild once min

And open your browser in `resources/public/index.html`. You will not
get live reloading, nor a REPL. 

## License

Copyright © 2014 FIXME

Distributed under the Eclipse Public License version 1.0.
