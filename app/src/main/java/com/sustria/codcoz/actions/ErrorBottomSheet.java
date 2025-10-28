package com.sustria.codcoz.actions;

import android.content.Context;
import android.view.LayoutInflater;

import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.sustria.codcoz.databinding.BottomSheetErrorBinding;

public class ErrorBottomSheet {

    public static void show(Context context, String title, String message) {
        BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(context);
        BottomSheetErrorBinding binding = BottomSheetErrorBinding.inflate(LayoutInflater.from(context));

        binding.tvErrorTitle.setText(title);
        binding.tvErrorMessage.setText(message);

        binding.btnFechar.setOnClickListener(v -> bottomSheetDialog.dismiss());
        bottomSheetDialog.setContentView(binding.getRoot());
        bottomSheetDialog.show();
    }

    public static void showGenericError(Context context) {
        show(context, "Ops! Algo deu errado", "Tente novamente em alguns instantes");
    }

    public static void showAuthError(Context context) {
        show(context, "Erro de autenticação", "Verifique seus dados e tente novamente");
    }
}
