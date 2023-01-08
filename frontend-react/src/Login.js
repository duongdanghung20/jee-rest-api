import { useState } from "react";
import { useHistory } from "react-router-dom";
import { ReactComponent as LogoUnilim } from './imgs/logo-ul.svg';
import useFetch from "./useFetch";

const Login = ({logIn}) => {
    const [username, setUsername] = useState('');
    const [password, setPassword] = useState('');
    const [wrongAccount, setWrongAccount] = useState(false);
    const [wrongPassword, setWrongPassword] = useState(false);

    const history = useHistory();

    const {data: acc, isPending, error} = useFetch(`http://localhost:8080/auth/${username}`);

    const handleSubmit = (e) => {
        e.preventDefault();
        setWrongAccount(false);
        setWrongPassword(false);
        if (acc["username"] === "NoSuchUser") {
            setWrongAccount(true);
        }
        else {
            if (acc["password"] !== password) {
                setWrongPassword(true);
            }
            else {
                logIn(acc["role"], username);
                history.push('/');
            }
        }
    }

    return (
        <div className="dummy-container">
            <LogoUnilim transform="scale(0.5)"/>
            <div className="add">
                <h1>Login</h1>
                <form onSubmit={handleSubmit}>
                    <label>Username:</label>
                    <input 
                        type="text" 
                        required 
                        value={username} 
                        onChange={(e) => setUsername(e.target.value)}/>
                    <label>Password:</label>
                    <input 
                        type="password" 
                        required 
                        value={password} 
                        onChange={(e) => setPassword(e.target.value)}/>   
                    { !isPending && <button onClick={handleSubmit}>Log in</button> }
                    { isPending && <button disabled>Logging in...</button> }
                    { wrongAccount && <div className="wrong">Account does not exist</div> }
                    { wrongPassword && <div className="wrong">Wrong password</div> }
                </form>
            </div>
        </div>
    );
}

export default Login;