import { AppBar, Toolbar, Typography, Button, Box, Container } from '@mui/material';
import { useNavigate } from 'react-router-dom';
import authService from '../services/auth';

interface LayoutProps {
    children: React.ReactNode;
}

export default function Layout({ children }: LayoutProps) {
    const navigate = useNavigate();
    const username = authService.getUsername();

    const handleLogout = async () => {
        await authService.logout();
        navigate('/login');
    };

    return (
        <Box
            sx={{
                display: 'flex',
                flexDirection: 'column',
                justifyContent: 'flex-start',
                alignItems: 'center',
                maxHeight: 'calc(100% - 2.0rem)',
                height: '100%',
                maxWidth: 'calc(100% - 2.0rem)',
                width: '100%',
                margin: 'auto',
                backgroundColor: '#f5f5f5',
                outline: '2px solid red',
            }}
        >
            <AppBar position="static">
                <Toolbar>
                    <Typography variant="h6" component="div" sx={{ flexGrow: 1 }}>
                        RML Manager
                    </Typography>
                    <Typography variant="body1" sx={{ mr: 2 }}>
                        {username}
                    </Typography>
                    <Button color="inherit" onClick={handleLogout}>
                        Logout
                    </Button>
                </Toolbar>
            </AppBar>
            <Container maxWidth="lg" sx={{ mt: 4 }}>
                {children}
            </Container>
        </Box>
    );
}