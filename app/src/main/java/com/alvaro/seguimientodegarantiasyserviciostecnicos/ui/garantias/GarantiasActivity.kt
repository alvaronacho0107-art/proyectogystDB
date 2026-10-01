package com.alvaro.seguimientodegarantiasyserviciostecnicos.ui.garantias

import com.alvaro.seguimientodegarantiasyserviciostecnicos.R
import com.alvaro.seguimientodegarantiasyserviciostecnicos.ui.maintainers.MaintainerActivity

class GarantiasActivity : MaintainerActivity() {
    override val entity = "garantias"
    override val layoutResource = R.layout.activity_garantias
}

