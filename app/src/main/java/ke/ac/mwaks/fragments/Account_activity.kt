package ke.ac.mwaks.fragments

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RelativeLayout
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.cardview.widget.CardView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import ke.ac.mwaks.R
import ke.ac.mwaks.util.FragmentButtonToActivityClickListener

class Account_activity : Fragment() {

    private lateinit var mlockUploads: RelativeLayout
    private lateinit var mlockDownloads: RelativeLayout
    private lateinit var mlockaccountSummary: RelativeLayout
    private lateinit var mlockOthers: RelativeLayout
    private lateinit var munlockFunctionalitiesBtn: CardView

    private lateinit var fragmentButtonToActivityClickListener: FragmentButtonToActivityClickListener

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is FragmentButtonToActivityClickListener)
            fragmentButtonToActivityClickListener = context
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val view = inflater.inflate(R.layout.fragment_account_activity, container, false)
        mlockUploads = view.findViewById(R.id.lockUploads)
        mlockDownloads = view.findViewById(R.id.lockDownloads)
        mlockaccountSummary = view.findViewById(R.id.lockaccountSummary)
        mlockOthers = view.findViewById(R.id.lockOthers)
        munlockFunctionalitiesBtn = view.findViewById(R.id.unlockFunctionalitiesBtn)
        val demoMode =
            requireActivity().getSharedPreferences("appmode", AppCompatActivity.MODE_PRIVATE)
                .getString("demomode", "true")

        if (demoMode.equals("true")) {
            // show app with locked functions
            Toast.makeText(requireContext(), "App in demo mode.", Toast.LENGTH_SHORT).show()
            mlockUploads.setOnClickListener { showOpenAccountToast() }
            mlockDownloads.setOnClickListener { showOpenAccountToast() }
            mlockaccountSummary.setOnClickListener { showOpenAccountToast() }
            mlockOthers.setOnClickListener { showOpenAccountToast() }

            munlockFunctionalitiesBtn.setOnClickListener {
                fragmentButtonToActivityClickListener.onButtonClicked()
            }

        } else {
            // remove locked functions
            munlockFunctionalitiesBtn.visibility = View.GONE
            mlockUploads.visibility = View.GONE
            mlockDownloads.visibility = View.GONE
            mlockaccountSummary.visibility = View.GONE
            mlockOthers.visibility = View.GONE
        }

        //initialize fields

        return view
    }

    fun showOpenAccountToast() {
        Toast.makeText(
            requireActivity().applicationContext,
            "Unlock account to explore",
            Toast.LENGTH_SHORT
        ).show()
    }

}

