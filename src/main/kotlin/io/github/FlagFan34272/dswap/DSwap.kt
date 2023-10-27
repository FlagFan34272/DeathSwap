package io.github.FlagFan34272.dswap

import io.github.monun.kommand.kommand
import net.kyori.adventure.text.Component.text
import net.kyori.adventure.text.format.NamedTextColor
import org.bukkit.Bukkit
import org.bukkit.Sound
import org.bukkit.entity.Player
import org.bukkit.plugin.java.JavaPlugin

class DSwap: JavaPlugin() {
    var isOn = false
    override fun onEnable() {
        kommand {
            "game" {
                requires { isOp && !isOn && server.onlinePlayers.size == 2 }
                "start" {
                    executes {
                        server.setWhitelist(true)
                        try {
                            var other: Player? = null
                            server.onlinePlayers.forEach{
                                it.sendTitle("데스 스왑 시작","")
                                it.playSound(it.location, Sound.ENTITY_ENDER_DRAGON_GROWL, 1f, 1f)
                                it.isWhitelisted = true

                                if (it == player) return@forEach
                                other = it!!
                            }


                            player.sendMessage(text("상대 플레이어: ${other!!.name}", NamedTextColor.GREEN))
                            other!!.sendMessage(text("상대 플레이어: ${player.name}", NamedTextColor.GREEN))
                            var countdown = 300

                            Bukkit.getScheduler().runTaskTimer(this@DSwap, Runnable {
                                if (countdown in 0..10) {
                                    server.onlinePlayers.forEach {
                                        it.sendMessage(text("카운트다운: $countdown", NamedTextColor.GREEN))
                                        it.playSound(it.location, Sound.BLOCK_NOTE_BLOCK_BELL, 1f, 1f)
                                    }
                                    if (countdown == 0) {
                                        player.teleport(other!!.location)
                                        other!!.teleport(player.location)
                                        server.onlinePlayers.forEach {
                                            it.sendMessage(text("스왑하는중..", NamedTextColor.GREEN))
                                        }
                                        countdown = 300
                                    }
                                }
                                countdown--
                            },0L, 20L)


                        } catch (e: Exception) {
                            e.printStackTrace()
                            isOn = false
                            server.setWhitelist(false)
                        }
                    }
                }
            }
        }
    }
    override fun onDisable() {
        server.setWhitelist(false)
    }
}
