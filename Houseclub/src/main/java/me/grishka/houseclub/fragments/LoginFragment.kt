package me.grishka.houseclub.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.Toast
import com.rilixtech.widget.countrycodepicker.CountryCodePicker
import me.grishka.appkit.Nav
import me.grishka.appkit.api.ErrorResponse
import me.grishka.appkit.api.SimpleCallback
import me.grishka.houseclub.R
import me.grishka.houseclub.api.BaseResponse
import me.grishka.houseclub.api.ClubhouseSession
import me.grishka.houseclub.api.methods.CompletePhoneNumberAuth
import me.grishka.houseclub.api.methods.ResendPhoneNumberAuth
import me.grishka.houseclub.api.methods.StartPhoneNumberAuth

class LoginFragment : BaseToolbarFragment() {
    private var phoneInput: EditText? = null
    private var codeInput: EditText? = null
    private var resendBtn: Button? = null
    private var nextBtn: Button? = null
    private var countryCodePicker: CountryCodePicker? = null
    private var resendCodeLayout: LinearLayout? = null
    private var sentCode = false

    override fun onCreateContentView(inflater: LayoutInflater, container: ViewGroup, savedInstanceState: Bundle?): View {
        val view = inflater.inflate(R.layout.login, container, false)
        phoneInput = view.findViewById(R.id.phone_input)
        codeInput = view.findViewById(R.id.code_input)
        resendBtn = view.findViewById(R.id.resend_code)
        nextBtn = view.findViewById(R.id.next)
        countryCodePicker = view.findViewById(R.id.country_code_picker)
        resendCodeLayout = view.findViewById(R.id.resend_code_layout)
        codeInput?.visibility = View.GONE
        resendBtn?.visibility = View.GONE
        codeInput?.visibility = View.GONE;
        resendCodeLayout?.visibility = View.GONE
        nextBtn?.setOnClickListener{ onNextClick(it) }
        resendBtn?.setOnClickListener{ onResendClick(it) }
        return view
    }

    private val cleanPhoneNumber: String
        private get() {
            val number = phoneInput!!.text.toString().replace("[^\\d]".toRegex(), "")
            return "+$number"
        }

    private fun onNextClick(v: View) {
        if (sentCode) {
            CompletePhoneNumberAuth(cleanPhoneNumber, codeInput!!.text.toString())
                .wrapProgress(activity)
                .setCallback(object : SimpleCallback<CompletePhoneNumberAuth.Response?>(this) {
                    override fun onSuccess(result: CompletePhoneNumberAuth.Response?) {
                        ClubhouseSession.userToken = result?.authToken
                        ClubhouseSession.userID = result?.userProfile!!.userId.toString() + ""
                        ClubhouseSession.isWaitlisted = result.isWaitlisted
                        ClubhouseSession.write()
                        if (result.isWaitlisted) {
                            Nav.goClearingStack(activity, WaitlistedFragment::class.java, null)
                        } else if (result.userProfile!!.username == null) {
                            Nav.goClearingStack(activity, RegisterFragment::class.java, null)
                        } else {
                            Nav.goClearingStack(activity, HomeFragment::class.java, null)
                        }
                    }
                })
                .exec()
        } else {
            StartPhoneNumberAuth(cleanPhoneNumber)
                .wrapProgress(activity)
                .setCallback(object : SimpleCallback<BaseResponse?>(this) {
                    override fun onSuccess(result: BaseResponse?) {
                        sentCode = true
                        phoneInput?.isEnabled = false
                        countryCodePicker?.isEnabled = false
                        codeInput?.visibility = View.VISIBLE
                        resendCodeLayout?.setVisibility(View.VISIBLE)
                    }

                    override fun onError(error: ErrorResponse?) {
                        Toast.makeText(context, "Login failed", Toast.LENGTH_SHORT).show()
                    }
                })
                .exec()
        }
    }

    private fun onResendClick(v: View) {
        ResendPhoneNumberAuth(cleanPhoneNumber)
            .wrapProgress(activity)
            .exec()
    }
}