import { homeIcon } from "../assets/icons"
import { DotLottieReact } from '@lottiefiles/dotlottie-react';


const ErrorPage = () => {
  return (
    <section className="relative padding-x pt-40 sm:pt-60 min-h-screen">
      <section className="absolute w-full h-full left-0 right-0 top-0 bottom-0-0 bg-hero bg-cover bg-center"></section>
      <section className="absolute w-full h-full left-0 right-0 top-0 bottom-0-0 bg-[#101211]/[93%]"></section>
      <section className="max-container relative">

        <div className="flex items-center flex-col">
          <DotLottieReact
            className="max-w-[900px]"
            src="https://lottie.host/0afe0385-5779-44e2-b18d-497aa61ac028/TKdG2Imnlh.lottie"
            loop
            autoplay
          />
          <p className="mt-8 font-inter text-white text-[16px] mb-6 text-center">Sorry we are unable to find the page</p>
          <a className="font-inter flex gap-2 px-[14px] py-[6px] bg-primary rounded-[8px]  hover:border-primary hover:shadow-[0_0_15px_#00FA55] transition-shadow duration-300" href="/">
            <img src={homeIcon} alt="home icon" />
            Go Home
          </a>
        </div>

      </section>
    </section>
  );
}

export default ErrorPage;
