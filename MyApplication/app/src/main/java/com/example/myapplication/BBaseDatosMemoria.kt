package com.example.myapplication

class BBaseDatosMemoria {

    companion object{
        val arregloBEntrenador = arrayListOf<BEntrenador>()

        init {
            arregloBEntrenador
                .add(
                    BEntrenador(1,"Adrian","a@a.com")
                )

            arregloBEntrenador
                .add(
                    BEntrenador(2,"Vicente","b@a.com")
                )

            arregloBEntrenador
                .add(
                    BEntrenador(3,"Carolina","c@a.com")
                )
        }
    }
}