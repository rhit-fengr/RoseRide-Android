package edu.rosehulman.roseride

import android.graphics.Color
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import androidx.navigation.navOptions
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.color.MaterialColors
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import edu.rosehulman.roseride.model.History
import edu.rosehulman.roseride.model.HistoryViewModel
import edu.rosehulman.roseride.model.Ride
import edu.rosehulman.roseride.model.RideViewModel
import edu.rosehulman.roseride.ui.rideList.RideListFragment
import java.text.SimpleDateFormat
import java.util.*

class RideAdapter(fragment: RideListFragment) : RecyclerView.Adapter<RideAdapter.RideViewHolder>() {
    val model = ViewModelProvider(fragment.requireActivity()).get(RideViewModel::class.java)
    val modelHistory = ViewModelProvider(fragment.requireActivity()).get(HistoryViewModel::class.java)
    val fragment = fragment


    inner class RideViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){

        val title: TextView = itemView.findViewById(R.id.row_ride_title)
        val dAddr: TextView = itemView.findViewById(R.id.row_ride_address)
        val time: TextView = itemView.findViewById(R.id.row_ride_set_off_time)
        val sAddr: TextView = itemView.findViewById(R.id.row_ride_set_off_address)
        val cost: TextView = itemView.findViewById(R.id.row_ride_cost)
        val numOfSlots: TextView = itemView.findViewById(R.id.row_ride_num_of_slots)
        val editbtn: ImageView =  itemView.findViewById(R.id.row_ride_rightImage)


        init {

            Log.d("size", model.size().toString())
            val current = getCurrentDateTime()
            val formatter = SimpleDateFormat("yyyy-MM-dd", Locale.US)
            val formatted = formatter.format(current)
            model.rides.forEach {
                if(formatted > it.setOffDate){
                    switchToHistory(it)
                }
            }

            itemView.setOnClickListener{
                // navigate
                model.updateCurrentPos(adapterPosition)
                itemView.findNavController().navigate(R.id.nav_ride_detail, null,
                    navOptions {
                        anim {
                            enter = android.R.anim.slide_in_left
                            exit = android.R.anim.slide_out_right
                        }
                    })
            }

            editbtn.setOnClickListener {
                model.updateCurrentPos(adapterPosition)
                itemView.findNavController().navigate(R.id.nav_ride_edit, null,
                    navOptions {
                        anim {
                            enter = android.R.anim.slide_in_left
                            exit = android.R.anim.slide_out_right
                        }
                    })
            }

            itemView.setOnLongClickListener{
                model.updateCurrentPos(adapterPosition)
                model.toggleCurrentRide()
                notifyDataSetChanged()
//                notifyItemChanged(adapterPosition)
                true
            }
        }

        fun bind(r: Ride) {
            editbtn.visibility=View.GONE
            if(MainActivity.driverMode && model.getCurrentRide().driver.equals(Firebase.auth.uid)){
                Log.d("whatthe", model.getCurrentRide().driver)
                editbtn.visibility=View.VISIBLE
            }

            title.text = r.title
            dAddr.text = "Address: "+r.destinationAddr.toString()
            time.text = "Set-off Time: "+ r.setOffDate.toString() + " " + r.setOffTime.toString().substring(0, r.setOffTime.toString().length-3)
            sAddr.text = "Set-off Address: "+r.pickUpAddr.toString()
            cost.text = "Cost/Person ($): "+r.costPerPerson.toString()
            numOfSlots.text = "Available slots: "+r.numOfSlots.toString()
            Log.d("RR","isSelected: ${r.isSelected}")

            val selected = MaterialColors.getColor(
                fragment.requireContext(),
                R.attr.colorAccent,
                Color.WHITE
            )

            val unselected = MaterialColors.getColor(
                fragment.requireContext(),
                R.attr.colorSurface,
                Color.WHITE
            )

            val color = if (r.isSelected) selected else unselected
            itemView.setBackgroundColor(color)
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RideViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.row_ride, parent, false)
        return RideViewHolder(view)
    }

    override fun onBindViewHolder(holder: RideViewHolder, position: Int) {
        holder.bind(model.getRideAt(position))
    }

    override fun getItemCount() = model.size()

    fun addRide(ride: Ride?) {
        model.addRide(ride)
        Log.d("RR","Size: ${model.size()}")
        notifyDataSetChanged()
    }

    fun addAllListener(fragmentName: String) {
        model.addAllListener(fragmentName,{
            notifyDataSetChanged()
        })
    }

    fun addOneListener(fragmentName: String) {
        model.addOneListener(fragmentName,{
            notifyDataSetChanged()
        })
    }

    fun removeListener(fragmentName: String) {
        model.removeListener(fragmentName)
    }

    fun getCurrentDateTime(): Date {
        return Calendar.getInstance().time
    }

    fun switchToHistory(ride: Ride?) {
        var history = History(
            title = ride?.title!!,
            driver = ride?.driver,
            setOffTime = ride?.setOffTime,
            setOffDate = ride?.setOffDate,
            destinationAddr = ride?.destinationAddr,
            costPerPerson = ride?.costPerPerson,
            passengers = ride?.passengers
        )

        model.removeRide(ride)
        modelHistory.addHistory(history)
    }

}