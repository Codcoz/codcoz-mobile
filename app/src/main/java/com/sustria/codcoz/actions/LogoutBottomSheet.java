package com.sustria.codcoz.actions;

import android.content.Context;
import android.view.LayoutInflater;

import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.sustria.codcoz.databinding.BottomSheetLogoutBinding;

public class LogoutBottomSheet {

    public interface LogoutCallback {
        void onConfirmLogout();
    }

    public static void show(Context context, LogoutCallback callback) {
        BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(context);
        BottomSheetLogoutBinding binding = BottomSheetLogoutBinding.inflate(LayoutInflater.from(context));

        binding.btnCancelar.setOnClickListener(v -> bottomSheetDialog.dismiss());
        binding.btnConfirmarSair.setOnClickListener(v -> {
            bottomSheetDialog.dismiss();
            if (callback != null) {
                callback.onConfirmLogout();
            }
        });

        bottomSheetDialog.setContentView(binding.getRoot());
        bottomSheetDialog.show();
    }
}

