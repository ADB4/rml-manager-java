import { Button, Stack } from "@mui/material";
import React, { useState } from 'react';
import AssetFormView from "./form/AssetFormView.tsx";
import type { AssetFormProps } from "../types/AssetForm.types.ts";

export const CreateAssetForm: React.FC = () => {

    return (
        <>
        </>
    )
}
export const CreateAssetComponent: React.FC = () => {
    // @ts-ignore
    const [successMessage, setSuccessMessage] = useState<string>('');
    // @ts-ignore
    const [showSuccess, setShowSuccess] = useState(false);

    const handleFormSubmit = async (data: AssetFormProps) => {
        console.log('Form submitted with data:', data);
        setSuccessMessage('Form submitted successfully!');
        setShowSuccess(true);
        // call api
    };
    return (
        <Stack sx={{
            width: '100%',
            height: '100%',
            backgroundColor: 'lightgray'
        }}>
            <Stack
                sx={{
                    height: 'calc(100% - 3.0rem)',
                    width: '100%',
                    backgroundColor: 'blue',
                }}>
                <AssetFormView onSubmit={handleFormSubmit}/>
            </Stack>
            <Stack
                direction={"row"}
                justifyContent={"space-between"}
                alignItems={"center"}
                sx={{
                    height: '3.0rem',
                    width: 'calc(100% - 2.0rem)',
                    margin: 'auto',
                }}>
                <Stack direction={"row"}>
                    ASSET INFO
                </Stack>
                <Stack
                    direction={"row"}
                    gap={1}
                >
                    <Button
                        variant={"contained"}
                        sx={{
                            height: '2.0rem',
                            width: '8rem',
                            borderRadius: '1.0rem',
                            border: '1px solid black',
                            backgroundColor: '#D9D9D9',
                            color: 'black',
                            fontFamily: 'MDIO',
                            fontSize: '1.0rem',
                            letterSpacing: '-0.025rem',
                        }}
                    >
                        SAVE DRAFT
                    </Button>
                    <Button variant={"contained"}
                            sx={{
                                height: '2.0rem',
                                width: '104px',
                                borderRadius: '1.0rem',
                                border: '1px solid black',
                                backgroundColor: '#69FF43',
                                color: 'black',
                                fontFamily: 'MDIO',
                                fontSize: '1.0rem',
                                letterSpacing: '-0.025rem',
                            }}
                    >
                        DEPLOY
                    </Button>
                </Stack>
            </Stack>
        </Stack>
    )
}