package com.example.wakeonlan.objects
import java.io.*
import java.lang.Exception
import java.lang.IllegalArgumentException
import java.net.*

object WakeOnLan {
    private const val PORT = 9

    @Throws(IllegalArgumentException::class)
    private fun getMacBytes(macStr: String): ByteArray {
        val hex = macStr.split("-",":")

        if (hex.size != 6) {
            throw IllegalArgumentException("Invalid MAC address.")
        }

        return try {
            hex.map { it.toInt(16).toByte() }.toByteArray()
        } catch (e: NumberFormatException) {
            throw IllegalArgumentException("Invalid hex digit in MAC address.")
        }
    }



    @JvmStatic
    fun main (args:Array<String>){
       // val args = arrayOf("123.123.123.123", "B0-7B-25-1F-27-B0")

        println(args)
        if (args.size != 2){
            println("java WakeOnLan <broadcast-ip> <mac-address>")
            //System.exit(1)
        }
        val ipStr = args[0]
        val macStr = args[1]

        try {
            val macBytes = getMacBytes(macStr)
            val bytes = ByteArray(6 + 16* macBytes.size)
            // add the first 6 bytes 0xFF - we can do a loop here
            for( i in 0 until 6){
                bytes[i] = 0xff.toByte()
            }
            for(i in 6 until bytes.size step macBytes.size){
                macBytes.copyInto(bytes,i,0,macBytes.size)
            }
            val address = InetAddress.getByName(ipStr)
            val packet = DatagramPacket(bytes,bytes.size,address, PORT)
            val socket = DatagramSocket()
            socket.send(packet)
            socket.close()

        }
        catch (e:Exception){
            println("Failed to send Wake-on-Lan packet: $e")
            //System.exit(1)

        }
    }

}