package com.n26.cointracker

import android.app.Application
import com.n26.cointracker.di.initKoin

class BitcoinWatchApp : Application() {
	override fun onCreate() {
		super.onCreate()
		initKoin()
	}
}
