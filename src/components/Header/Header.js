function Header() {
    return (
        <>
            <header className="header">
                <nav className="navbar navbar-expand-md navbar-dark bg-dark">
                    <div className="container-fluid">
                        <a className="navbar-brand helvetica-medium" href="/dashboard">Product CordApp</a>

                        <button className="navbar-toggler" type="button" data-bs-toggle="collapse" data-bs-target="#navbarSupportedContent" aria-controls="navbarSupportedContent" aria-expanded=" false" aria-label="Togglenavigation">
                            <span className="navbar-toggler-icon"> </span>
                        </button>
                        <div className="collapse navbar-collapse justify-content-end helvetica-light" id="navbarSupportedContent">
                            <ul className="navbar-nav me-3 mb-2 mb-Ig-0">
                                <li className="nav-item">
                                    <a className="nav-link active" aria-current="page" href='/'>Home</a>
                                </li>
                                <li className="nav-item">
                                    <a className="nav-link" href="/">Settings</a>
                                </li>
                                <li className="nav-item">
                                    <a className="nav-link" href="/">Help</a>
                                </li>
                            </ul >
                            <form className="d-flex">
                                <input className="form-control me-1" type="search" placeholder="Search" aria-label="Search" />
                                <button className="btn btn-outline-light" type="submit">Search</button>
                                <a className="nav-link" href="/login">SignOut</a>
                            </form>
                        </div >
                    </div >
                </nav >
            </header >
        </>

    )
}
export default Header;
