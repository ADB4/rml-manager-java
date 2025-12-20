import React from 'react';
import { Controller, type Control, type FieldErrors } from 'react-hook-form';
import {
    FormControl,
    InputLabel,
    Select,
    MenuItem,
    FormHelperText,
    // SelectChangeEvent,
} from '@mui/material';
import type {AssetFormProps} from "../../../types/AssetForm.types.ts";

interface FormSelectFieldProps {
    name: keyof AssetFormProps;
    control: Control<AssetFormProps>;
    errors: FieldErrors<AssetFormProps>;
    label: string;
    options: { value: string; label: string }[];
    disabled?: boolean;
}

export const FormSelectField: React.FC<FormSelectFieldProps> = ({
    name,
    control,
    errors,
    label,
    options,
    disabled = false,
}) => {
    return (
        <Controller
            name={name}
            control={control}
            render={({ field }) => (
                <FormControl fullWidth margin="normal" error={!!errors[name]}>
                    <InputLabel>{label}</InputLabel>
                    <Select
                        {...field}
                        label={label}
                        disabled={disabled}
                    >
                        {options.map((option) => (
                            <MenuItem key={option.value} value={option.value}>
                                {option.label}
                            </MenuItem>
                        ))}
                    </Select>
                    {errors[name] && (
                        <FormHelperText>{errors[name]?.message as string}</FormHelperText>
                    )}
                </FormControl>
            )}
        />
    );
};