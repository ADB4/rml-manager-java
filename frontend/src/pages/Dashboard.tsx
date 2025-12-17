import { useEffect, useState } from 'react';
import {
    Typography,
    Button,
    Paper,
    Table,
    TableBody,
    TableCell,
    TableContainer,
    TableHead,
    TableRow,
    CircularProgress,
    Box,
} from '@mui/material';
import api from '../services/api';
import Layout from '../components/Layout';
import {CreateAssetComponent} from "../components/CreateAssetComponent.tsx";

interface Asset {
    itemId: string;
    itemName: string;
    category: string;
    subcategory: string;
    description: string;
    material: string;
    animation: boolean;
    lods: string;
}

export default function Dashboard() {
    const [assets, setAssets] = useState<Asset[]>([]);
    const [loading, setLoading] = useState(true);
    const [error, setError] = useState('');

    useEffect(() => {
        fetchAssets();
    }, []);

    const fetchAssets = async () => {
        try {
            setLoading(true);
            const response = await api.get<Asset[]>('/assets');
            setAssets(response.data);
            setError('');
        } catch (err: any) {
            setError('Failed to load assets');
            console.error(err);
        } finally {
            setLoading(false);
        }
    };

    if (loading) {
        return (
            <Layout>
                <Box sx={{ display: 'flex', justifyContent: 'center', mt: 4 }}>
                    <CircularProgress />
                </Box>
            </Layout>
        );
    }

    if (error) {
        return (
            <Layout>
                <Typography color="error">{error}</Typography>
            </Layout>
        );
    }

    return (
        <Layout>
            <Box sx={{ display: 'flex', columnGap: '2.0rem', width: '100%', height: '100%', backgroundColor: 'light-blue',}}>
                <Box sx={{
                    margin: 'auto',
                    marginLeft: '0rem',
                    display: 'flex',
                    flexDirection: 'column',
                    rowGap: '0.5rem',
                    height: '100%',
                    minWidth: '12.0rem',
                    justifyContent: 'flex-start',
                    alignItems: 'center',
                    outline: '2px solid blue',
                }}>
                    <Typography variant="h5">CONSOLE</Typography>
                    <Button variant="contained" onClick={() => alert('Add asset - coming soon!')}>
                        CREATE ASSET
                    </Button>
                </Box>
                <Box sx={{
                    width: '100%',
                    height: '100%',
                    maxHeight: '100%',
                    outline: '2px solid red',
                }}>
                    <CreateAssetComponent />
                </Box>
            </Box>
        </Layout>
    );
}