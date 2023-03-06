# clodl

Play wordle in the terminal with clodl.

## Usage

### Using the binary
```
Clodl - Wordle in the terminal

It is required to provide the COLUMNS shell variable
in order to provide a seamless user experience.

COLUMNS=$COLUMNS clodl [COMMAND] ... [WORDLIST]

Usage: 

         play    Play a game of clodl
         help    Display this very page

Examples:

        COLUMNS=$COLUMNS clodl play words.txt
        clodl help
```

### Using leiningen
```
Clodl - Wordle in the terminal

It is required to provide the COLUMNS shell variable
in order to provide a seamless user experience.

COLUMNS=$COLUMNS lein run [COMMAND] ... [WORDLIST]

Usage: 

         play    Play a game of clodl
         help    Display this very page

Examples:

        COLUMNS=$COLUMNS lein run play words.txt
        lein run help
```

## License

clodl (c) 2023 Jakob Waibel and contributors

SPDX-License-Identifier: AGPL-3.0