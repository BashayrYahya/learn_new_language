package com.example.learn_new_language.videoCall

import android.Manifest.permission.*
import android.app.AlertDialog
import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import com.example.learn_new_language.databinding.VideoCallFragmentBinding
import android.content.pm.PackageManager
import android.util.Log
import android.widget.Toast



import androidx.core.app.ActivityCompat.requestPermissions


const val PERMISSION_REQ_ID_CAMERA =1
const val PERMISSION_REQ_ID_RECORD_AUDIO =2

class VideoCallFragment : Fragment() {

    private lateinit var binding: VideoCallFragmentBinding

    private val viewModelCallCam: VideoCallViewModel by lazy { ViewModelProvider(this)[VideoCallViewModel::class.java] }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        binding = VideoCallFragmentBinding.inflate(layoutInflater)


        makeVideoCall()

        viewModelCallCam.initializeAndJoinChannel(
            requireContext(),
            binding.localVideoViewContainer, binding.remoteVideoViewContainer
        )

        binding.btnCall.setOnClickListener {
            //videoCallViewModel.endCall(binding.localVideoViewContainer, mLocalView = binding., mRemoteContainer = binding.remoteVideoViewContainer, mRemoteView = binding.videoCallRL)
        }
        binding.btnSwitchCamera.setOnClickListener {
            viewModelCallCam.onSwitchCameraClicked(it)
        }
        binding.btnMute.setOnClickListener {
            viewModelCallCam.onLocalAudioMuteClicked(it,true,binding.btnMute)
        }

//        binding.btnCall.setOnClickListener {
//            viewModel.endCall(mLocalView, mLocalContainer, mRemoteView, mRemoteContainer)
//        }
//        binding.remoteVideoViewContainer.setOnClickListener {
//            viewModel.onRemoteUserVideoMuted(uid,muted,container)
//        }


        return binding.root
    }
    private fun makeVideoCall() {
        checkForPermission(CAMERA, "camera", PERMISSION_REQ_ID_CAMERA)
        checkForPermission(RECORD_AUDIO, "audio", PERMISSION_REQ_ID_RECORD_AUDIO)
        viewModelCallCam.initializeAndJoinChannel(
            requireContext(),
            binding.localVideoViewContainer, binding.remoteVideoViewContainer
        )
    }

    private fun checkForPermission(permission: String, name: String, requestCode: Int) {
        when {
            ContextCompat.checkSelfPermission(
                requireContext(),
                permission
            ) == PackageManager.PERMISSION_GRANTED -> {
                Log.d("permission", "$name permission granted")
            }
            shouldShowRequestPermissionRationale(permission) -> showDialog(
                permission,
                name,
                requestCode
            )

            else -> requestPermissions(requireActivity(), arrayOf(permission), requestCode)
        }
    }

    private fun showDialog(permission: String, name: String, requestCode: Int) {

        val builder = AlertDialog.Builder(requireContext())
        builder.apply {
            setMessage(" permission to access your $name is required to use this app")
            setTitle("Permission required ")
            setPositiveButton("Ok") { _, _ ->
                requestPermissions(requireActivity(), arrayOf(permission), requestCode)
            }
        }
        val dialog: AlertDialog = builder.create()
        dialog.show()
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        fun innerCheck(name: String) {
            if (grantResults.isEmpty() || grantResults[0] != PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(requireContext(), "$name permission refused ", Toast.LENGTH_LONG)
                    .show()
            } else {
                Toast.makeText(requireContext(), "$name permission granted ", Toast.LENGTH_LONG)
                    .show()
            }
        }
        when (requestCode) {
            PERMISSION_REQ_ID_CAMERA -> innerCheck("camera")
            PERMISSION_REQ_ID_RECORD_AUDIO -> innerCheck("audio")
        }
    }

}

