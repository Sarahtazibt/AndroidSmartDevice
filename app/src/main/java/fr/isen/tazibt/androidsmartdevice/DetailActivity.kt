package fr.isen.tazibt.androidsmartdevice

import android.annotation.SuppressLint
import android.bluetooth.BluetoothDevice
import android.bluetooth.BluetoothGatt
import android.bluetooth.BluetoothGattCallback
import android.bluetooth.BluetoothProfile
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import fr.isen.tazibt.androidsmartdevice.databinding.ActivityDetailBinding

@SuppressLint("MissingPermission")
class DetailActivity : AppCompatActivity() {
    private lateinit var binding: ActivityDetailBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val bluetoothDevice: BluetoothDevice? = intent.getParcelableExtra("device")
        val bluetoothGatt = bluetoothDevice?.connectGatt(this, false, bluetoothGattCallback)
        bluetoothGatt?.connect()
    }

    override fun onStop() {
        super.onStop()
        //bluetoothGatt?.connect()
    }


    private val bluetoothGattCallback = object : BluetoothGattCallback() {
        override fun onConnectionStateChange(gatt: BluetoothGatt?, status: Int, newState: Int) {
            if (newState == BluetoothProfile.STATE_CONNECTED) {
                displayContentConnected()

                // successfully connected to the GATT Server
            }
            //else if (newState == BluetoothProfile.STATE_DISCONNECTED) {
            // disconnected from the GATT Server }
        }
    }

    private fun displayContentConnected() {

        //binding.Text.text = getString(R.string.device_led_text)
        //binding.progressBar.isVisible = false
        //binding.LEDon.isvible = true
    }
}