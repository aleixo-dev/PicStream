package com.nicolas.picstream.downloader

interface Downloader {
    fun downloadFile (url : String, title : String) : Long
    fun downloadVideo (url : String, title : String) : Long
}