import { ReactComponent as PersonIcon } from './imgs/person-fill.svg';

const Navbar = ({logOut, username}) => {
    return (
        <nav className="navbar">
            <div className="avatar"></div>
            <div id="welcome"><PersonIcon transform='scale(1.65)' style={{"marginRight": "1%"}}/> {username}</div>
            <div id="logout" onClick={logOut}>Logout</div>
        </nav>
    );
}
 
export default Navbar;