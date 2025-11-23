import { ThemeProvider, createTheme } from '@mui/material/styles';
import './App.css';
import CssBaseline from '@mui/material/CssBaseline';
import { Container, AppBar, Toolbar, Typography, Box } from '@mui/material';

const theme = createTheme({
    palette: {
        mode: 'light',
        primary: {
            main: '#1976d2',
        },
        secondary: {
            main: '#dc004e',
        },
    },
});

function App() {
    return (
        <ThemeProvider theme={theme}>
            <div className={"App"}>
                    <Typography variant="h4" sx={{margin: "auto", outline: "2px solid red"}}gutterBottom>
                        Welcome to Your App
                    </Typography>
            </div>

        </ThemeProvider>
    );
}

export default App;