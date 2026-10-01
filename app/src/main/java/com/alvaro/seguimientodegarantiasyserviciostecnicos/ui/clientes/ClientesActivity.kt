package com.alvaro.seguimientodegarantiasyserviciostecnicos.ui.clientes

import com.alvaro.seguimientodegarantiasyserviciostecnicos.R
import com.alvaro.seguimientodegarantiasyserviciostecnicos.ui.maintainers.MaintainerActivity

class ClientesActivity : MaintainerActivity() {
    override val entity = "clientes"
    override val layoutResource = R.layout.activity_clientes
}

