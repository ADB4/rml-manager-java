import { useForm } from 'react-hook-form';
import { yupResolver } from '@hookform/resolvers/yup';
// import { AssetFormProps } from '../types/AssetForm.types';
// import { AssetFormSchema } from '../validation/assetForm.schema';
import { type AssetFormProps} from "../../../types/AssetForm.types.ts";
import { AssetFormSchema } from "../../../validation/assetForm.schema.ts";

export const useAssetFormViewModel = (
    onSubmit: (data: AssetFormProps) => void,
    initialValues?: Partial<AssetFormProps>
) => {
    const defaultValues: AssetFormProps = {
        itemcode: '',
        itemname: '',
        category: '',
        subcategory: '',
        material: '',
        lodcount: [],
        description: '',
        version: '1.0.0',
        ...initialValues,
    };

    const {
        control,
        handleSubmit,
        formState: { errors, isSubmitting, isDirty, isValid },
        reset,
        watch,
        setValue,
    } = useForm<AssetFormProps>({
        resolver: yupResolver(AssetFormSchema),
        defaultValues,
        mode: 'onChange',
    });

    const handleFormSubmit = handleSubmit(async (data) => {
        try {
            await onSubmit(data);
        } catch (error) {
            console.error('Form submission error:', error);
        }
    });

    const handleReset = () => {
        reset(defaultValues);
    };

    const watchCategory = watch('category');
    const watchLodCount = watch('lodcount');

    return {
        control,
        errors,
        isSubmitting,
        isDirty,
        isValid,
        handleFormSubmit,
        handleReset,
        reset,
        watch,
        setValue,
        watchCategory,
        watchLodCount,
    };
};