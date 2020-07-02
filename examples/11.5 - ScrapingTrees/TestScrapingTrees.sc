import $file.ScrapingTrees, ScrapingTrees._

pprint.log(commentsTree)

val expected = Seq(
  Comment(
    "adamo",
    "Catching up with work that I did not do during the workweek.",
    List(
      Comment(
        "notagoodidea",
        "Let me join that team too. The grape of servers which I planned to use to run some models and make some calculations is down after a security breach. So I am spinning those in my laptop with its poor 4 cores. I am thinking about getting a cloud instance because I am on a short timing. Or I can see if the fan of my laptop can make it levitate.",
        List()
      )
    )
  ),
  Comment(
    "yumaikas",
    "Resting, cleaning a few things, and learning Elixir and Phoenix. I\u2019m planning on porting kb_wiki from Nim to Elixir, as a learning exercise, and then I\u2019d like to make a mini-arcade for showing off my Godot projects next week. After I get comfortable with that, it\u2019ll be on to working on the custom E-commerce site for my wife.",
    List()
  ),
  Comment(
    "srid",
    "Integrating codemirror to cerveau\u2019s editor, and improving the editing experience.",
    List()
  ),
  Comment(
    "erthink",
    "Implementing C++ binding for libmdbx",
    List(Comment("mrr", "Welcome to Lobster.rs!", List()))
  ),
  Comment(
    "pyj",
    "Rolling into my (semi-)annual \u201cpersonal dev week hackathon\u201d to use some PTO.",
    List(
      Comment(
        "mrr",
        "Looks like a lot of fun!",
        List(
          Comment(
            "pyj",
            "I was probably going to do Mersenne Twister and then probably Mother-Of-All.",
            List(
              Comment(
                "JoshuaRLi",
                "https://www.pcg-random.org/download.html#minimal-c-implementation",
                List(
                  Comment(
                    "pyj",
                    "Cool! I heard pcg was short, but never knew it was that short!",
                    List()
                  )
                )
              )
            )
          )
        )
      )
    )
  ),
  Comment(
    "cadey",
    "Dungeon World! I\u2019m working on a setting to DM in and I\u2019ve assembled a group for a test fire tonight. I\u2019m also gonna figure out more about how to use OneNote and also how to make dungeon maps on computers.",
    List(
      Comment(
        "tbolt",
        "I had a lot of fun playing Dungeon World. It sounds like you\u2019re going to make a great setting/story, have fun!",
        List()
      )
    )
  ),
  Comment("zgrep", "Playing Ordinal Markup.", List()),
  Comment(
    "AndroidKitKat",
    "Waiting anxiously for my new laptop to come in the mail. Finally pulled the trigger on monday for a new 2020 MacBook Pro. Got the 10th gen Intel model and I\u2019m looking forward to it.",
    List()
  ),
  Comment(
    "DanilaFe",
    "Working on the 12th part of the functional compiler series. I had really hoped to get it done earlier, but there are a lot of changes I had to make to the compiler code, and I\u2019m trying to walk through all of them, and that takes time. Also coming back home from uni, since I just graduated with a BS in Computer Science!",
    List(
      Comment(
        "soc",
        "Interesting! Do you have plans on writing something about namespacing? It feels most texts just paper over it.",
        List()
      )
    )
  ),
  Comment(
    "varjag",
    "So I rigged a sio2usb cable last weekend, means going to enjoy some quality 8-bit Atari gaming. Alley Cat Boulder Dash Montezuma\u2019s Revenge Bruce Lee Ninja River Raid International Karate",
    List()
  ),
  Comment(
    "pietroppeter",
    "Attending this: https://lobste.rs/s/ggozoa/nim_online_conference_2020",
    List()
  ),
  Comment(
    "TomMD",
    "Prepping for a launch so\u2026. Roll out new production Migrate a database Increase the ceiling to our cloud capacity I\u2019m sure there\u2019s twelve things on the list and another 12 I should know about but haven\u2019t considered.",
    List()
  ),
  Comment(
    "rjpcasalino",
    "Headed over to CHOP in a bit to study the fringes. I\u2019m curious if the center is growing and staying strong. Otherwise, I think I might dive into The UNIX Book.",
    List()
  ),
  Comment(
    "zem",
    "i plan to poke around the dreamwidth code - i really want to see if some of the components can be tested in isolation, which would massively decrease the barrier to contributions. realistically i\u2019ll probably spend most of the time learning about the current perl ecosystem :)",
    List()
  ),
  Comment(
    "jorgelbg",
    "I\u2019ll continue working on my pet project, which is an alternative to collect statistics from my personal blog. It is still in the early stages but It has already proven quite usefu for my needs. Since I don\u2019t feel like writing a frontend and I wanted to play with the data I\u2019m using Grafana for the visualization (this is how the overview dashboard looks like: https://share.getcloudapp.com/ApuAGgbm). As a bonus this works without adding any Javascript to the website (my blog is hosted using Github Pages).",
    List()
  ),
  Comment("reezer", "Work and explore the tildeverse.", List()),
  Comment(
    "JoshuaRLi",
    "Working on epub-optimizer. It\u2019s basically a fancy wrapper around the dance of unzipping an epub archive and running fd -e png -x pngoptim ... etc, and works pretty well as of 38c5434. I\u2019m currently trying to port the external call to zip to zip-rs but have run into an issue. Also for maximum convenience on linux/amd64, I plan to bundle the external programs (jpegoptim, pngquant, minify) behind a feature flag so the user doesn\u2019t have to install them. Already got the static building done nicely: https://github.com/joshuarli/static-builders/releases/tag/20.6.19",
    List()
  ),
  Comment(
    "0x70532007",
    "There\u2019s an old Russian joke about scientists needing to have five mistresses in additon to their wife, so that they could tell their wife they\u2019re going to see the mistress, tell the mistress they\u2019re with their wife, and then go to the lab to do their work. A few weeks ago, partner and I made plans to visit nearby town and friends. Then I made alternate plans to go camping, but my ride fell through. As a result, I\u2019m here with the keyboard, just me and the wild Brooklyn fireworks for June 19. I\u2019ll be continuing work on my prototype decentralized forum, or community framework, if you will. It always feels like I\u2019m only a couple more changes away from MVP, but the goalposts also move with every user test. There are a few problems with account creation. Also, new item creation times need to be reduced. Browser support is pretty good at this time, except the gap in supporting those without JS and without cookes and with no PHP on the server. In this case, user account creation is currently not possible, and only unsigned posting can be done.",
    List()
  )
)

assert(commentsTree == expected)
