package com.submission.zelsis.ui.profile

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import com.submission.zelsis.R
import com.submission.zelsis.databinding.FragmentProfileBinding
import com.submission.zelsis.ui.welcome.WelcomeActivity
import com.submission.zelsis.util.ViewModelFactory
import com.submission.zelsis.util.getImageUri


class ProfileFragment : Fragment() {

    private var _binding: FragmentProfileBinding? = null
    private val binding get() = _binding!!
    private var currentImageUri: Uri? = null

    private val viewModel: ProfileViewModel by viewModels {
        ViewModelFactory.getInstance(requireActivity())
    }

    private val requestPermissionLauncher =
        registerForActivityResult(
            ActivityResultContracts.RequestPermission()
        ) { isGranted: Boolean ->
            if (isGranted) {
                Toast.makeText(
                    requireActivity(),
                    R.string.permission_request_granted, Toast.LENGTH_SHORT
                ).show()
            } else {
                Toast.makeText(
                    requireActivity(),
                    R.string.permission_request_denied, Toast.LENGTH_SHORT
                ).show()
            }
        }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentProfileBinding.inflate(inflater, container, false)
        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        viewModel.getName()
        setName()

        viewModel.getEmail()
        setEmail()

        viewModel.getImageUri()
        setImage()

        binding.ibEdit.setOnClickListener {
            if (!cameraPermissionsGranted()) {
                requestPermissionLauncher.launch(REQUIRED_PERMISSION)
            } else {
                startCamera()
            }
        }

        binding.btnLogout.setOnClickListener {
            actionLogout()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun actionLogout(){
        viewModel.logout()
        val intent = Intent(requireActivity(), WelcomeActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
        startActivity(intent)

    }

    private fun setName() {
        viewModel.name.observe(viewLifecycleOwner, Observer { name ->
            binding.tvName.text = "Name : $name"
        })
    }

    private fun setEmail() {
        viewModel.email.observe(viewLifecycleOwner, Observer { email ->
            binding.tvEmail.text = "Email : $email"
        })
    }

    private fun cameraPermissionsGranted() =
        ContextCompat.checkSelfPermission(
            requireContext(),
            REQUIRED_PERMISSION
        ) == PackageManager.PERMISSION_GRANTED

    private val launcherCamera = registerForActivityResult(
        ActivityResultContracts.TakePicture()
    ) { isSuccess ->
        if (isSuccess) {
            currentImageUri?.let {
                viewModel.saveImageUri(it.toString())
                binding.ivProfile.setImageURI(it)
            }
        }
    }

    private fun setImage() { // Add this function
        viewModel.imageUri.observe(viewLifecycleOwner) { uri ->
            if (uri?.isNotEmpty() == true) {
                binding.ivProfile.setImageURI(Uri.parse(uri))
            }
        }
    }


    private fun startCamera() {
        currentImageUri = getImageUri(requireContext())
        launcherCamera.launch(currentImageUri!!)
    }

    companion object {
        private const val REQUIRED_PERMISSION = Manifest.permission.CAMERA
    }

}