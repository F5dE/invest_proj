package com.f5de.invest

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.EditText
import android.widget.Spinner
import androidx.fragment.app.Fragment
import com.google.gson.GsonBuilder
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.IOException
import java.io.InputStream


/**
 * A simple [Fragment] subclass as the second destination in the navigation.
 */
class SimulationFragment : Fragment() {

    var controller: Controller? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_simulation, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val editText = view.findViewById<EditText>(R.id.simulation_days)
        controller = Controller.getInstance(requireContext())
        view.findViewById<Button>(R.id.simulation_start).setOnClickListener {
            controller?.start(editText.text.toString().toInt())
            //TODO Simulation
        }
    }


}