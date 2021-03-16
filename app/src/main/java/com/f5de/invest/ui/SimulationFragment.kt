package com.f5de.invest.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.f5de.invest.Controller
import com.f5de.invest.R
import com.f5de.invest.data.*


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
        val editTextMoney = view.findViewById<EditText>(R.id.simulation_money)
        controller = Controller.getInstance(requireContext())
        view.findViewById<Button>(R.id.simulation_start).setOnClickListener {
            if (editText.text.toString().toInt() > 0 && editTextMoney.text.toString().toInt() > 0)
                controller?.setSimulation(editText.text.toString().toInt(), editTextMoney.text.toString().toInt())
            else
                Toast.makeText(requireContext(), "Enter correct values!", Toast.LENGTH_SHORT).show()
        }
    }


}