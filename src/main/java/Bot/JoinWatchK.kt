package Bot

import sx.blah.discord.handle.obj.IUser
import sx.blah.discord.handle.obj.IVoiceChannel
import java.util.*

class JoinWatchK(val bot:BroddaBot):Thread(){

    /**
     * To be run in the bakground and checks all channels, if a 'BRUDDA' joins,
     * the bot joins the channel and plays a theme son (JIGGA JIGGA)
     * Trying to do it in Kotlin cuz im funky
     * */
    val SCAN_PERIOD_S = 2
    var joinedUsers = getAllConnectedUsers()
    val userSongs = createUsersSongs()

    override fun run(){
        while(true){
            println("Lifebeat theme scan...")
            Thread.sleep((SCAN_PERIOD_S*1000).toLong())
            val currentlyConnectedUsers = getAllConnectedUsers()
            val freshUsers = getNewJoinedUsers(currentlyConnectedUsers)
            joinedUsers = currentlyConnectedUsers
            if(freshUsers.size>0)
                println("Freshlyjoined users")
            for(user in freshUsers){
                println(user)
                if(userSongs.containsKey(user.toLowerCase())){
                    println("HAS THEMESONG!")
                    playThemeSong(user)
                }
            }
        }
    }

    private fun playThemeSong(userName:String){
        val songUrl = userSongs.get(userName.toLowerCase())
        for(vChannel in bot.allConnectedVoiceChannels){
            val users = vChannel.connectedUsers.filter({ iUser: IUser ->iUser.name == userName})
            if(users.size>0){
                val user = users[0]
                val vch = bot.findChannelWithUser(user)
                if(!bot.busyPlayingIn(vch)) {
                    val songUrl = userSongs.get(user.name)!!.songUrl
                    val dur = userSongs.get(user.name)!!.durations
                    println("PLAYIGN SON")
                    println(songUrl)
                    bot.playSong(songUrl, vch)
                    val timer = ThemeSongTimer(bot.getVoiceChannelPlayer(vch), dur)
                    timer.start()

                } else{
                    println("Bot is busy in channel, not playing themesong... :(")
                }
                return
            }
        }


    }

    private fun getNewJoinedUsers(newUsers:List<String>):List<String>{
        val res = LinkedList<String>()
        for(user in newUsers){
            if(!joinedUsers.contains(user)){
                res.add(user)
            }
        }
        return res
    }

    private fun getAllConnectedUsers():List<String>{
        val res = LinkedList<String>()
        for(ch in bot.allConnectedVoiceChannels){
            val usersInCh = ArrayList<String>()
            ch.connectedUsers.map { user:IUser -> usersInCh.add(user.name) }
            if(usersInCh.isNotEmpty())
                res.addAll(usersInCh)
        }
        return res
    }

    private fun joinAndPlay(user:String, channel: IVoiceChannel){
        println("PLAYING THEMESONG FOR USER: "+ user)
    }

    /**
     * Create the map with users that have THEME songs
     * */

    private fun createUsersSongs():HashMap<String,ThemeSong>{
        val res = HashMap<String,ThemeSong>()

        res.put("vandplog", ThemeSong("https://www.youtube.com/watch?v=TXgpwKfmsGg",20))
        res.put("nisse", ThemeSong("https://youtu.be/w2XMBN1o2fk?t=40s",20))
        res.put("whoppr", ThemeSong("https://youtu.be/geic-ci56xg?t=5s",20))
        res.put("hypp", ThemeSong("https://youtu.be/F3HK628agYI?t=32s",20))

        return res
    }

}

class ThemeSong(val songUrl:String, val durations:Int)

class ThemeSongTimer(val channelPlyr:ChannelPlayer, val duration:Int):Thread(){

    override fun run() {
        println("Timer started")
        Thread.sleep((duration*1000).toLong())
        channelPlyr.next()
        println("Timer ended")
    }
}
