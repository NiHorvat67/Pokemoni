import { NavLink, useLocation } from "react-router-dom";
import { useEffect, useRef, useState } from "react";
import profileImg from "../assets/images/profile.jpeg";
import useAuthContext from "@/hooks/useAuthContext";
import Button from "@/components/Button";
import { useNavigate } from "react-router-dom";

const Nav = () => {
  const location = useLocation();
  const { user, dispatch } = useAuthContext();
  const navigate = useNavigate()

  const hideSingInButton = location.pathname.includes("/auth");

  const [desktopOpen, setDesktopOpen] = useState<boolean>(false);
  const [mobileOpen, setMobileOpen] = useState<boolean>(false);

  const desktopMenuRef = useRef<HTMLDivElement | null>(null);

  const logoutOnClick = () => {
    fetch("/api/accounts/logout", { credentials: "include" })
      .catch(() => { })
      .finally(() => {
        dispatch({ type: "LOGOUT" });
        navigate("/")
      });
  };

  useEffect(() => {
    const onClickOutside = (e: MouseEvent) => {
      if (!desktopMenuRef.current) return;
      if (desktopMenuRef.current.contains(e.target as Node)) return;
      setDesktopOpen(false);
    };
    if (desktopOpen) document.addEventListener("mousedown", onClickOutside);
    return () => document.removeEventListener("mousedown", onClickOutside);
  }, [desktopOpen]);

  useEffect(() => {
    setDesktopOpen(false);
    setMobileOpen(false);
  }, [location.pathname]);

  const role = String((user as any)?.accountRole ?? "").toLowerCase();
  const isAdmin = !!user && role === "admin";
  const isTrader = !!user && role === "trader";

  const userId = (user as any)?.accountId;

  const menuLinkClass =
    "relative inline-flex py-2 text-[17px] leading-none text-center font-medium " +
    "after:content-[''] after:absolute after:left-1/2 after:-translate-x-1/2 after:bottom-0 after:h-[2px] after:w-full " +
    "after:origin-center after:scale-x-0 hover:after:scale-x-100 after:transition-transform after:duration-300";

  const profileImgClass =
    "rounded-full h-10 w-10 sm:h-12 sm:w-12 object-cover border-2 transition-all duration-300 cursor-pointer " +
    (desktopOpen || mobileOpen
      ? "border-primary shadow-[0_0_15px_#00FA55]"
      : "border-white hover:border-primary hover:shadow-[0_0_15px_#00FA55]");

  const handleProfileClick = () => {
    if (window.innerWidth >= 640) setDesktopOpen((v) => !v);
    else setMobileOpen((v) => !v);
  };

  return (
    <header className="padding-x py-2.5 sm:py-3 absolute z-20 w-full bg-dark-bg/50 backdrop-blur-[6px]">
      <div className="flex items-center justify-between">
        <NavLink className="font-red-hat text-xl sm:text-[32px]" to="/">
          <span className="font-bold text-primary">Gear</span>
          <span className="text-white">Share</span>
        </NavLink>

        {!hideSingInButton && (
          user == null ? (
            <NavLink className="px-6 py-2 font-bold bg-primary hover:bg-white rounded-lg" to="/auth">
              Sign in
            </NavLink>
          ) : (
            <div className="relative" ref={desktopMenuRef}>
              <button
                type="button"
                onClick={handleProfileClick}
                aria-expanded={desktopOpen || mobileOpen}
                className="flex items-center justify-center cursor-pointer"
              >
                <img src={profileImg} alt="profile" className={profileImgClass} />
              </button>

              <div
                className={[
                  "hidden sm:block absolute right-0 mt-4 w-72",
                  "transition-opacity duration-200 ease-out",
                  desktopOpen ? "opacity-100 pointer-events-auto" : "opacity-0 pointer-events-none",
                ].join(" ")}
              >
                <div className="absolute right-6 -top-2 h-4 w-4 rotate-45 bg-white" />

                <div
                  className={[
                    "rounded-2xl bg-white shadow-xl ring-1 ring-black/5 overflow-hidden",
                    "origin-top transition-[max-height] duration-300 ease-out",
                    desktopOpen ? "max-h-[500px]" : "max-h-0",
                  ].join(" ")}
                >
                  <div className="px-6 pt-6 pb-5 text-center">
                    <div className="text-slate-900 font-semibold text-[19px]">
                      {`${(user as any)?.userFirstName ?? ""} ${(user as any)?.userLastName ?? ""}`.trim() || "User"}
                    </div>
                  </div>

                  <div className="px-6 pb-6 text-center text-slate-700 flex flex-col items-center">
                    <NavLink
                      to={userId != null ? `/profile/${userId}` : "/error"}
                      className={`${menuLinkClass} after:bg-slate-300`}
                      reloadDocument
                    >
                      My Profile
                    </NavLink>

                    {isTrader && (
                      <NavLink to="/new-advertisement" className={`${menuLinkClass} after:bg-slate-300`}>
                        Add advertisement
                      </NavLink>
                    )}

                    {isAdmin && (
                      <NavLink to="/admin" className={`${menuLinkClass} after:bg-slate-300`}>
                        Admin Panel
                      </NavLink>
                    )}

                    <div className="mt-7 flex justify-center">
                      <div className="inline-flex">
                        <Button
                          text="Logout"
                          icon={false}
                          long={false}
                          onClick={logoutOnClick}
                          className="justify-center hover:shadow-[0_0_15px_#00FA55]"
                        />
                      </div>
                    </div>
                  </div>
                </div>
              </div>
            </div>
          )
        )}
      </div>

      <div
        className={[
          "sm:hidden overflow-hidden",
          "transition-[max-height,opacity,transform] duration-300 ease-out",
          mobileOpen ? "max-h-[420px] opacity-100 translate-y-0" : "max-h-0 opacity-0 -translate-y-1",
        ].join(" ")}
      >
        <div className="pt-6 pb-6">
          <div className="flex flex-col items-center gap-4 text-white">
            <NavLink
              to={userId != null ? `/profile/${userId}` : "/error"}
              onClick={() => setMobileOpen(false)}
              className={`${menuLinkClass} after:bg-white`}
              reloadDocument
            >
              My Profile
            </NavLink>

            {isTrader && (
              <NavLink
                to="/new-advertisement"
                onClick={() => setMobileOpen(false)}
                className={`${menuLinkClass} after:bg-white`}
              >
                Add advertisement
              </NavLink>
            )}

            {isAdmin && (
              <NavLink
                to="/admin"
                onClick={() => setMobileOpen(false)}
                className={`${menuLinkClass} after:bg-white`}
              >
                Admin Panel
              </NavLink>
            )}

            <div className="pt-4 flex justify-center">
              <div className="inline-flex">
                <Button
                  text="Logout"
                  icon={false}
                  long={false}
                  onClick={() => {
                    setMobileOpen(false);
                    logoutOnClick();
                  }}
                  className="justify-center hover:shadow-[0_0_15px_#00FA55]"
                />
              </div>
            </div>
          </div>
        </div>
      </div>
    </header>
  );
};

export default Nav;
