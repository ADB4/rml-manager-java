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
            }}
        >
            <AppBar position="relative">
                <Toolbar
                    sx={{
                    backgroundColor: 'white',
                    color: 'black',
                }}>
                    <Typography
                        variant="h2"
                        component="div"
                        sx={{
                            flexGrow: 1,
                            fontFamily: 'Swiss721',
                            fontSize: '1.5rem',
                    }}>
                        RML Manager
                    </Typography>
                    <Typography
                        variant="body1"
                        sx={{
                            mr: 2,
                            fontFamily: 'MDIO',
                            fontSize: '1.1rem',
                    }}>
                        {username?.toUpperCase()}
                    </Typography>
                    <Button
                        color="inherit"
                        onClick={handleLogout}
                        style={{
                            width: '6.0rem',
                            height: '2.0rem',
                            borderRadius: '1.0rem',
                            fontFamily: 'MDIO',
                            fontSize: '1.1rem',
                            backgroundColor: 'black',
                            color: 'white',
                        }}>
                        Logout
                    </Button>
                </Toolbar>
            </AppBar>
            <Container maxWidth="xl"
                       sx={{
                           margin: 'auto',
                           maxHeight: 'calc(100% - 2.0rem)',
                           height: 'calc(100% - 8.0rem)',
                       }}>
                {children}
            </Container>
        </Box>
    );
}