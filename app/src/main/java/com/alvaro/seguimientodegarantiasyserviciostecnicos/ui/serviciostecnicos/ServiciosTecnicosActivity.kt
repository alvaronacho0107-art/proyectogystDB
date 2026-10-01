package com.alvaro.seguimientodegarantiasyserviciostecnicos.ui.serviciostecnicos

import com.alvaro.seguimientodegarantiasyserviciostecnicos.R
import com.alvaro.seguimientodegarantiasyserviciostecnicos.ui.maintainers.MaintainerActivity

class ServiciosTecnicosActivity : MaintainerActivity() {
    override val entity = "servicios"
    override val layoutResource = R.layout.activity_servicios_tecnicos
}

