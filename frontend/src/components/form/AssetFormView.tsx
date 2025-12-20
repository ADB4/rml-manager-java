import React from 'react';
import {
    Box,
    Paper,
    Typography,
    Button,
    Grid,
    CircularProgress,
} from '@mui/material';
import SaveIcon from '@mui/icons-material/Save';
import RestartAltIcon from '@mui/icons-material/RestartAlt';
import { type AssetFormProps } from "../../types/AssetForm.types.ts";
import { useAssetFormViewModel } from "./viewmodel/useAssetFormViewModel.ts";
import { FormTextField } from "./fields/FormTextField.tsx";
import { FormSelectField } from "./fields/FormSelectField.tsx";
import { FormLodCountField } from "./fields/FormLodCountField.tsx";

export interface AssetFormViewProps {
    onSubmit: (data: AssetFormProps) => void;
    initialValues?: Partial<AssetFormProps>;
    isLoading?: boolean;
}

const categoryOptions = [
    { value: 'furniture', label: 'Furniture' },
    { value: 'electronics', label: 'Electronics' },
    { value: 'accessories', label: 'Accessories' },
    { value: 'tools', label: 'Tools' },
];

const subcategoryOptions = [
    { value: 'indoor', label: 'Indoor' },
    { value: 'outdoor', label: 'Outdoor' },
    { value: 'commercial', label: 'Commercial' },
];

const materialOptions = [
    { value: 'wood', label: 'Wood' },
    { value: 'metal', label: 'Metal' },
    { value: 'plastic', label: 'Plastic' },
    { value: 'composite', label: 'Composite' },
    { value: 'glass', label: 'Glass' },
];

const AssetFormView: React.FC<AssetFormViewProps> = ({
                                                                onSubmit,
                                                                initialValues,
                                                                isLoading = false,
                                                            }) => {
    const {
        control,
        errors,
        isSubmitting,
        isDirty,
        isValid,
        handleFormSubmit,
        handleReset,
    } = useAssetFormViewModel(onSubmit, initialValues);

    return (
        <Paper elevation={3} sx={{ p: 4, maxWidth: 800, mx: 'auto', my: 4 }}>
            <Typography variant="h4" component="h1" gutterBottom>
                Model Form
            </Typography>

            <Typography variant="body2" color="text.secondary" sx={{ mb: 3 }}>
                Fill in the details for your model
            </Typography>

            <Box component="form" onSubmit={handleFormSubmit} noValidate>
                <Grid container spacing={3}>
                    <Grid>
                        <FormTextField
                            name="itemcode"
                            control={control}
                            errors={errors}
                            label="Item Code"
                            placeholder="Enter item code"
                            disabled={isLoading || isSubmitting}
                        />
                    </Grid>

                    <Grid>
                        <FormTextField
                            name="itemname"
                            control={control}
                            errors={errors}
                            label="Item Name"
                            placeholder="Enter item name"
                            disabled={isLoading || isSubmitting}
                        />
                    </Grid>
                    <Grid>
                        <FormSelectField
                            name="category"
                            control={control}
                            errors={errors}
                            label="Category"
                            options={categoryOptions}
                            disabled={isLoading || isSubmitting}
                        />
                    </Grid>

                    <Grid>
                        <FormSelectField
                            name="subcategory"
                            control={control}
                            errors={errors}
                            label="Subcategory"
                            options={subcategoryOptions}
                            disabled={isLoading || isSubmitting}
                        />
                    </Grid>
                    <Grid>
                        <FormSelectField
                            name="material"
                            control={control}
                            errors={errors}
                            label="Material"
                            options={materialOptions}
                            disabled={isLoading || isSubmitting}
                        />
                    </Grid>

                    <Grid>
                        <FormTextField
                            name="version"
                            control={control}
                            errors={errors}
                            label="Version"
                            placeholder="e.g., 1.0.0"
                            disabled={isLoading || isSubmitting}
                        />
                    </Grid>
                    <Grid>
                        <FormLodCountField
                            control={control}
                            errors={errors}
                            disabled={isLoading || isSubmitting}
                        />
                    </Grid>
                    <Grid>
                        <FormTextField
                            name="description"
                            control={control}
                            errors={errors}
                            label="Description"
                            placeholder="Enter a detailed description"
                            multiline
                            rows={4}
                            disabled={isLoading || isSubmitting}
                        />
                    </Grid>
                    <Grid>
                        <Box sx={{ display: 'flex', gap: 2, justifyContent: 'flex-end' }}>
                            <Button
                                variant="outlined"
                                startIcon={<RestartAltIcon />}
                                onClick={handleReset}
                                disabled={!isDirty || isLoading || isSubmitting}
                            >
                                Reset
                            </Button>

                            <Button
                                type="submit"
                                variant="contained"
                                startIcon={
                                    isSubmitting ? <CircularProgress size={20} /> : <SaveIcon />
                                }
                                disabled={!isDirty || !isValid || isLoading || isSubmitting}
                            >
                                {isSubmitting ? 'Submitting...' : 'Submit'}
                            </Button>
                        </Box>
                    </Grid>
                </Grid>
            </Box>
        </Paper>
    );
};
export default AssetFormView;