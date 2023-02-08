import { useEffect,useState } from "react";
import axios from 'axios';

export default function Login(props) {

    const [hostname, setHostName] = useState('');
    const [port, setPort] = useState('');
    const [name, setName] = useState('');
    const [pass, setPass] = useState('');
    const [loggedIn, setLoggedIn] = useState(false);
    const [result, setResult] = useState(false);
    const url = "http://localhost:";

    useEffect(() => {
        result ? setLoggedIn(true) : setLoggedIn(false);
    }, [result])


    async function onSubmit(event) {
        event.preventDefault();
        try {
            console.log(hostname);
            console.log(port);
            console.log(name);
            console.log(pass);
            const res=await axios.get(url + '/currentuser');
            console.log(res.data);
            
                if (hostname == res.data.host && name == res.data.username && pass == res.data.pass && port == res.data.port) {
                    localStorage.setItem("isAuthenticaed",true);
                    localStorage.setItem("currentuser", hostname+":"+port);
                    setResult(res.data);
                    loggedIn ? window.location.pathname = "/login" : window.location.pathname = "/dashboard";
                }
        }
        catch { }
    }


    return (
        <main id="main">
            <section className="login-section">
                <div className="container-fluid">
                    <div className="row justify-content-center pt-5">
                        <div className="login-block bg-white m-5 p-Ig-5 p-3 col-Ig-5 col-md-5 col-sm-6 col-8">
                            <h3 className="text-center mb-4 fw-bold">Welcome to CordApp</h3>
                            <form className="login-form" onSubmit={(e) => onSubmit(e)}>
                                <div className="md-4">
                                    <label htmlFor="nodehostname" className="form-label">Node Hostname</label>
                                    <input type="text" className="form-control" id="username" value={hostname} onChange={(e) => setHostName(e.target.value)} required />
                                </div>
                                <div className="md-4">
                                    <label htmlFor="nodeport" className="form-label">Node Port</label>
                                    <input type="text" className="form-control" id="username" value={port} onChange={(e) => setPort(e.target.value)} required />
                                </div>
                                <div className="md-4">
                                    <label htmlFor="username" className="form-label">RPC User Name</label>
                                    <input type="text" className="form-control" id="username" value={name} onChange={(e) => setName(e.target.value)} required />
                                </div>
                                <div className="mb-4">
                                    <label htmlFor="password" className="form-label">RPC Password</label>
                                    <input type="password" className="form-comtrol" id="password" value={pass} onChange={(e) => setPass(e.target.value)} required />
                                </div>
                                <div className=" mb-4 d-flex justify-content-between align-items-center">
                                    <div className="form-check mb-0">
                                        <input className="form-check-input me-2" type="checkbox" value="remember-me" />
                                        <label className="form-check-label" htmlFor="remember-me">Remember me</label>
                                    </div>
                                    <a href="/" className="text-body">Forgot password?</a>
                                </div>
                                <div className="mb-2 d-flex justify-content-end ">
                                    <button type="submit" className="btn btn-primary">Login</button>
                                </div>
                            </form >
                        </div >
                    </div >
                </div >
            </section >
        </main >
    )
}