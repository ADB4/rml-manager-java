import * as yup from 'yup';

export const AssetFormSchema = yup.object().shape({
    itemcode: yup
        .string()
        .required('Item code is required')
        .min(3, 'Item code must be at least 3 characters')
        .max(50, 'Item code must not exceed 50 characters'),

    itemname: yup
        .string()
        .required('Item name is required')
        .min(3, 'Item name must be at least 3 characters')
        .max(100, 'Item name must not exceed 100 characters'),

    category: yup
        .string()
        .required('Category is required'),

    subcategory: yup
        .string()
        .required('Subcategory is required'),

    material: yup
        .string()
        .required('Material is required'),

    lodcount: yup
        .array()
        .of(yup.number().required().positive('LOD count must be positive'))
        .min(1, 'At least one LOD count is required')
        .required('LOD count is required'),

    description: yup
        .string()
        .required('Description is required')
        .min(10, 'Description must be at least 10 characters')
        .max(500, 'Description must not exceed 500 characters'),

    version: yup
        .string()
        .required('Version is required')
        .matches(/^\d+\.\d+\.\d+$/, 'Version must be in format X.Y.Z (e.g., 1.0.0)'),
});