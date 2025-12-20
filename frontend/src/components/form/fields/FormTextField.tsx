import React from 'react';
import { Controller, type Control, type FieldErrors } from 'react-hook-form';
import { TextField } from '@mui/material';
import type {AssetFormProps} from "../../../types/AssetForm.types.ts";

interface FormTextFieldProps {
    name: keyof AssetFormProps;
    control: Control<AssetFormProps>;
    errors: FieldErrors<AssetFormProps>;
    label: string;
    placeholder?: string;
    multiline?: boolean;
    rows?: number;
    type?: string;
    disabled?: boolean;
}

export const FormTextField: React.FC<FormTextFieldProps> = ({
    name,
    control,
    errors,
    label,
    placeholder,
    multiline = false,
    rows = 1,
    type = 'text',
    disabled = false,
    }) => {
    return (
        <Controller
            name={name}
            control={control}
            render={({ field }) => (
                <TextField
                    {...field}
                    label={label}
                    placeholder={placeholder}
                    fullWidth
                    multiline={multiline}
                    rows={rows}
                    type={type}
                    disabled={disabled}
                    error={!!errors[name]}
                    helperText={errors[name]?.message as string}
                    variant="outlined"
                    margin="normal"
                />
            )}
        />
    );
};