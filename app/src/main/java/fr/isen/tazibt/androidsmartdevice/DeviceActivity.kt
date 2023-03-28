package fr.isen.tazibt.androidsmartdevice

import android.Manifest
import android.annotation.SuppressLint
import android.bluetooth.*
import android.content.pm.PackageManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.Toast
import androidx.core.app.ActivityCompat
import java.util.*


class DeviceActivity : AppCompatActivity() {

    private lateinit var bluetoothGatt: BluetoothGatt
    private var ledCharacteristic: BluetoothGattCharacteristic? = null
    private var clickCharacteristic: BluetoothGattCharacteristic? = null

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_device)

        // Récupérer l'appareil sélectionné dans la liste
        val device: BluetoothDevice = intent.getParcelableExtra("device")!!

        // Se connecter à l'appareil
        if (ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.BLUETOOTH_CONNECT
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return
        }
        bluetoothGatt = device.connectGatt(this, false, gattCallback)

        // Afficher les différentes actions
        findViewById<Button>(R.id.LEDon).setOnClickListener {
            // Vérifier que la caractéristique des LEDs est disponible
            if (ledCharacteristic == null) {
                Toast.makeText(this, "Caractéristique des LEDs non disponible", Toast.LENGTH_SHORT)
                    .show()
            } else {
                // Allumer les LEDs
                ledCharacteristic!!.value = byteArrayOf(0x01)
                bluetoothGatt.writeCharacteristic(ledCharacteristic)
            }
        }

        findViewById<Button>(R.id.click_button).setOnClickListener {
            // Vérifier que la caractéristique du nombre de clics est disponible
            if (clickCharacteristic == null) {
                Toast.makeText(
                    this,
                    "Caractéristique du nombre de clics non disponible",
                    Toast.LENGTH_SHORT
                ).show()
            } else {
                // Lire le nombre de clics
                bluetoothGatt.readCharacteristic(clickCharacteristic)
            }
        }
    }

    private val gattCallback = object : BluetoothGattCallback() {

        @SuppressLint("MissingPermission")
        override fun onConnectionStateChange(gatt: BluetoothGatt?, status: Int, newState: Int) {
            super.onConnectionStateChange(gatt, status, newState)

            if (newState == BluetoothProfile.STATE_CONNECTED) {
                // Découvrir les services et les caractéristiques

                gatt?.discoverServices()
            }
        }

        override fun onServicesDiscovered(gatt: BluetoothGatt?, status: Int) {
            super.onServicesDiscovered(gatt, status)

            if (status == BluetoothGatt.GATT_SUCCESS) {
                // Récupérer les caractéristiques
                val service3: BluetoothGattService? =
                    gatt?.getService(UUID.fromString("service3_uuid"))
                val service4: BluetoothGattService? =
                    gatt?.getService(UUID.fromString("service4_uuid"))

                if (service3 != null) {
                    ledCharacteristic =
                        service3.getCharacteristic(UUID.fromString("led_characteristic_uuid"))
                }

                if (service4 != null) {
                    clickCharacteristic =
                        service4.getCharacteristic(UUID.fromString("click_characteristic_uuid"))
                }
            }
        }

        override fun onCharacteristicRead(
            gatt: BluetoothGatt?,
            characteristic: BluetoothGattCharacteristic?,
            status: Int
        ) {
            super.onCharacteristicRead(gatt, characteristic, status)

            if (status == BluetoothGatt.GATT_SUCCESS) {
                // Afficher le nombre de clics
                val value = characteristic?.value
                val clicks = value?.get(0)?.toInt()
                Toast.makeText(this@DeviceActivity, "Nombre de clics : $clicks", Toast.LENGTH_SHORT)
                    .show()
            }
        }

        override fun onCharacteristicWrite(
            gatt: BluetoothGatt?,
            characteristic: BluetoothGattCharacteristic?,
            status: Int
        ) {
            super.onCharacteristicWrite(gatt, characteristic, status)

            if (status == BluetoothGatt.GATT_SUCCESS) {
                // LEDs allumées

            }
        }
    }
}