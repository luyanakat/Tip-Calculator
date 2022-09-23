package luyanakat.me.tiptest

import android.annotation.SuppressLint
import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.KeyEvent
import android.view.View
import android.view.inputmethod.InputMethodManager
import androidx.core.view.isVisible
import luyanakat.me.tiptest.databinding.ActivityMainBinding
import java.text.NumberFormat
import kotlin.math.ceil

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.sharedBillTip.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                binding.iconPayer.visibility = View.VISIBLE
                binding.sharedBillTipNumber.visibility = View.VISIBLE
                binding.sharedBillTipNumberAdd.visibility = View.VISIBLE
            } else {
                binding.iconPayer.visibility = View.GONE
                binding.sharedBillTipNumberAdd.visibility = View.GONE
                binding.sharedBillTipNumber.visibility = View.GONE
                binding.resultTotalPerPayer.text = ""
                binding.resultTipPerPayer.text = ""
                binding.resultTextTotal.text = ""
            }
        }
        binding.calculateBtn.setOnClickListener { calculatedTip() }
    }


    @SuppressLint("SetTextI18n")
    private fun calculatedTip() {
        val stringInTextField = binding.costOfServiceEditText.text.toString();
        val cost = stringInTextField.toDoubleOrNull()

        if (cost == null) {
            binding.resultText.text = ""
            binding.resultTextTotal.text = ""
            return
        }

        val tipPercentage = when (binding.tipOptions.checkedRadioButtonId) {
            R.id.option_amazing -> 0.20
            R.id.option_good -> 0.18
            else -> 0.15
        }

        var tip = tipPercentage * cost
        var total = tip + cost

        if (binding.switchTip.isChecked) {
            tip = ceil(tip)
        }

        val formattedTip = NumberFormat.getCurrencyInstance().format(tip)
        binding.resultText.text = getString(R.string.tip_amount, formattedTip)

        if (binding.sharedBillTip.isChecked) {
            val numberOfPayer = binding.sharedBillTipNumberAdd.text.toString().toIntOrNull()
            if (numberOfPayer == null) {
                binding.iconPayer.visibility = View.GONE
                binding.sharedBillTipNumberAdd.visibility = View.GONE
                binding.sharedBillTipNumber.visibility = View.GONE
                binding.resultTotalPerPayer.text = ""
                binding.resultTipPerPayer.text = ""
                return
            }
            var tipPerPayer = tip / numberOfPayer
            var totalPerPayer = cost / numberOfPayer + tipPerPayer
            if (binding.switchTip.isChecked) {
                tipPerPayer = ceil(tipPerPayer)
                totalPerPayer = ceil(totalPerPayer)
                total = ceil(total)
            }
            val formattedTipPerPayer = NumberFormat.getCurrencyInstance().format(tipPerPayer)
            val formattedTotalPerPayer = NumberFormat.getCurrencyInstance().format(totalPerPayer)
            val formattedTotal = NumberFormat.getCurrencyInstance().format(total)

            binding.resultTipPerPayer.text =  getString(R.string.tip_per_payer, formattedTipPerPayer)
            binding.resultTextTotal.text = getString(R.string.total_result, formattedTotal)
            binding.resultTotalPerPayer.text = getString(R.string.total_per_payer,
                formattedTotalPerPayer)
        }
    }
}
