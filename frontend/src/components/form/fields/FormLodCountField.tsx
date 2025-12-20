import React, { useState } from 'react';
import { Controller, type Control, type FieldErrors } from 'react-hook-form';
import {
    Box,
    TextField,
    IconButton,
    Chip,
    FormHelperText,
    Typography,
    Stack,
} from '@mui/material';
import AddIcon from '@mui/icons-material/Add';
// import DeleteIcon from '@mui/icons-material/Delete';
// import DeleteIcon from '@mui/icons-material/Delete';
import type {AssetFormProps} from "../../../types/AssetForm.types.ts";

interface FormLodCountFieldProps {
    control: Control<AssetFormProps>;
    errors: FieldErrors<AssetFormProps>;
    disabled?: boolean;
}

export const FormLodCountField: React.FC<FormLodCountFieldProps> = ({
    control,
    errors,
    disabled = false,
}) => {
    const [inputValue, setInputValue] = useState<string>('');

    return (
        <Controller
            name="lodcount"
            control={control}
            render={({ field }) => (
                <Box sx={{ my: 2 }}>
                    <Typography variant="subtitle2" gutterBottom>
                        LOD Counts
                    </Typography>

                    <Stack direction="row" spacing={1} alignItems="center">
                        <TextField
                            type="number"
                            value={inputValue}
                            onChange={(e) => setInputValue(e.target.value)}
                            placeholder="Enter LOD count"
                            size="small"
                            disabled={disabled}
                            inputProps={{ min: 1 }}
                            onKeyPress={(e) => {
                                if (e.key === 'Enter') {
                                    e.preventDefault();
                                    const num = parseInt(inputValue);
                                    if (!isNaN(num) && num > 0) {
                                        field.onChange([...field.value, num]);
                                        setInputValue('');
                                    }
                                }
                            }}
                        />
                        <IconButton
                            color="primary"
                            onClick={() => {
                                const num = parseInt(inputValue);
                                if (!isNaN(num) && num > 0) {
                                    field.onChange([...field.value, num]);
                                    setInputValue('');
                                }
                            }}
                            disabled={disabled || !inputValue}
                        >
                            <AddIcon />
                        </IconButton>
                    </Stack>

                    <Box sx={{ mt: 2, display: 'flex', flexWrap: 'wrap', gap: 1 }}>
                        {field.value.map((count, index) => (
                            <Chip
                                key={index}
                                label={count}
                                onDelete={
                                    disabled
                                        ? undefined
                                        : () => {
                                            const newValue = field.value.filter((_, i) => i !== index);
                                            field.onChange(newValue);
                                        }
                                }
                                color="primary"
                                variant="outlined"
                            />
                        ))}
                    </Box>

                    {errors.lodcount && (
                        <FormHelperText error>
                            {errors.lodcount.message as string}
                        </FormHelperText>
                    )}
                </Box>
            )}
        />
    );
};