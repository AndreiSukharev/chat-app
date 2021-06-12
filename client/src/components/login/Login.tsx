import React, {FormEvent, useCallback, useState} from 'react';
import {
    Select,
    MenuItem,
    Button,
    Avatar,
    Box,
    TextField,
    CssBaseline,
    Container,
    Typography,
} from "@material-ui/core";
import LockOutlinedIcon from '@material-ui/icons/LockOutlined';
import loginStyles from "./loginStyles";
import basicStyles from "../../basicStyles";
import {signInGitHubService} from "../../services/auth";

function Copyright() {
    return (
        <Typography variant="body2" color="textSecondary" align="center">
            {'Copyright © Облачные зайчики '}
            {new Date().getFullYear()}
        </Typography>
    );
}

const Login: React.FC = () => {
    const classes = basicStyles();
    const loginClasses = loginStyles();
    const [username, setUsername] = useState<string>('');
    const [password, setPassword] = useState<string>('');

    const isButtonDisabled = !username || !password;

    const signIn = async (event: FormEvent) => {
        event.preventDefault();
        alert("Стандартная аутентификация не доступна, используйте GitHub")
    }

    const signInGitHub = async (event: FormEvent) => {
        event.preventDefault();
        await signInGitHubService();
        console.log("after")
    }

    return (
        <Container component="main" maxWidth="xs">
            <CssBaseline/>
            <div className={classes.paper}>
                <Avatar className={loginClasses.avatar}>
                    <LockOutlinedIcon/>
                </Avatar>
                {/*<form onSubmit={signIn} className={classes.form} noValidate>*/}
                    <TextField
                        value={username}
                        onChange={(event) => setUsername(event.target.value)}
                        id="email"
                        variant="outlined"
                        margin="normal"
                        required
                        fullWidth
                        label="Имя пользователя"
                        autoComplete="username"
                        autoFocus
                    />
                    <TextField
                        value={password}
                        onChange={(event) => setPassword(event.target.value)}
                        id="password"
                        variant="outlined"
                        margin="normal"
                        required
                        fullWidth
                        name="password"
                        label="Пароль"
                        type="password"
                        autoComplete="current-password"
                    />

                    <Button
                        type="submit"
                        disabled={isButtonDisabled}
                        fullWidth
                        variant="contained"
                        color="primary"
                        className={classes.input}
                    >
                        Войти
                    </Button>

                    <Button
                        fullWidth
                        variant="contained"
                        color="primary"
                        onClick={signInGitHub}
                        className={classes.input}
                    >
                        Войти через GitHub
                    </Button>
                {/*</form>*/}
            </div>
            <Box mt={8}>
                <Copyright/>
            </Box>
        </Container>
    );
}


export default Login;