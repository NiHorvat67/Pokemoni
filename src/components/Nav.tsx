import { NavLink, useLocation } from "react-router-dom";
import profileImg from "../assets/images/profile.jpeg"
import useAuthContext from "@/hooks/useAuthContext";

const Nav = () => {
  const linkClass = ({ isActive }: { isActive: boolean }) => isActive ? "relative inline-block text-white py-2 leading-none after:content-[''] after:absolute after:left-1/2 after:-translate-x-1/2 after:bottom-[2px] after:h-[2px] after:w-full after:bg-white after:origin-center after:scale-x-120 after:transition-transform after:duration-300" : "relative inline-block text-white py-2 leading-none after:content-[''] after:absolute after:left-1/2 after:-translate-x-1/2 after:bottom-[2px] after:h-[2px] after:w-full after:bg-white after:origin-center after:scale-x-0 hover:after:scale-x-120 after:transition-transform after:duration-300";
  const location = useLocation();
  const { user } = useAuthContext();
  const hideSingInButton = location.pathname === "/auth";
  return (
    <header className="padding-x py-6 sm:py-3 absolute z-10 w-full bg-dark-bg/70 backdrop-blur-md">
      <div className="grid grid-cols-3 items-center">
        {/* kada se napravi dropdown na profilu, u div iznad dodati max-container klasu */}

        <div className="flex justify-start items-center">
          <NavLink className="font-red-hat text-2xl sm:text-[32px]" to="/">
            <span className="font-bold text-primary">Gear</span>
            <span className="text-white">Share</span>
          </NavLink>
        </div>

        <div className="flex justify-center items-center space-x-20 ">
          <NavLink className={linkClass} to="/error">Error</NavLink>
          <NavLink className={linkClass} to="/admin">Admin</NavLink>
          <NavLink className={linkClass} to="/new-advertisement">Advert</NavLink>
        </div>

        <div className="flex justify-end items-center h-[48px] sm:h-[52px]">
          {!hideSingInButton && (user == null ? (<NavLink className="px-6 py-2  font-bold bg-primary hover:bg-white rounded-lg" to="/auth">Sign in</NavLink>)
            :
            <NavLink className="flex justify-center items-center" to="/error">
              <img id="profile-image" src={profileImg} alt="profile"
                className="rounded-full h-10 w-10 sm:h-12 sm:w-12 object-cover border-2 border-white hover:border-primary hover:shadow-[0_0_15px_#00FA55] transition-shadow duration-300" />
            </NavLink>
          )}


        </div>

      </div>

    </header>
  );
};

export default Nav;
