<p align="center">
  <img src="https://github.com/itm-unipi/MyPodcastDB/blob/main/src/main/resources/img/logo.png?raw=true" alt="MyPodcastDB-Logo" height="64px"/>
</p>

# MyPodcastDB

Large-Scale and Multi-Structured Databases project.

The dumps of the databases and the source code can be found in the [release](https://github.com/itm-unipi/MyPodcastDB/releases) page. 

## Overview

**MyPodcastDB** is a social network that allows to keep in touch on podcasts, view rankings about the top rated podcasts, the most followed authors and so on. A **podcast** is an
episodic series of digital audio or video files. For each podcast, the application provides
a detailed page that contains information such as episodes, duration, release date and
reviews.

An **user** can like and review a podcast or add it to his watchlist. He can follow an author
and follow other users. These activities will be exploited to provide suggestions to the
user which can help him to find out new authors and podcasts.

On the other side an **author** can create, update and delete a podcast. He can use the
application as the user but he can’t follow users, write reviews, like a podcast or add it
in a watchlist. He can follow other authors.

**Admins** can manage users, authors, podcasts and reviews. They also have access to the
usage analytics of the application.

## Package Architecture

```
MyPodcastDB
├── docs
│   ├── MyPodcastDB-Documentation.pdf
│   ├── MyPodcastDB-Project-Presentation.pdf
│   └── MyPodcastDB-User-Manual.pdf
└── src
    ├── main
    │   ├── java
    │   │   └── it.unipi.dii.lsmsdb.myPodcastDB
    │   │       ├── cache
    │   │       ├── controller
    │   │       ├── model
    │   │       ├── persistence
    │   │       │   ├── mongo
    │   │       │   └── neo4j 
    │   │       ├── service
    │   │       ├── utility
    │   │       └── view
    │   └── resources    
    └── test
        └── java
            └── it.unipi.dii.lsmsdb.myPodcastDB
                ├── mongo
                └── neo4j
```

## Authors

* Biagio Cornacchia, b.cornacchia@studenti.unipi.it
* Gianluca Gemini, g.gemini@studenti.unipi.it
* Matteo Abaterusso, m.abaterusso@studenti.unipi.it