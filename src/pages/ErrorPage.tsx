import { homeIcon } from "../assets/icons"

const ErrorPage = () => {
  return (
    <section className="relative padding-x pt-40 sm:pt-60 min-h-screen">
      <section className="absolute w-full h-full left-0 right-0 top-0 bottom-0-0 bg-hero bg-cover bg-center"></section>
      <section className="absolute w-full h-full left-0 right-0 top-0 bottom-0-0 bg-[#101211]/[90%]"></section>
      <section className="max-container relative">

        <div className="flex items-start flex-col">
          <h1 className="font-red-hat text-[32px] sm:text-[40px] font-bold mb-5">
            <span className="text-primary">404</span>
            <span className="text-white"> Not Found</span>
          </h1>
          <p className="font-inter text-white text-[16px] mb-6">Sorry we are unable to find the page</p>
          <a className="font-inter flex gap-2 px-[14px] py-[6px] bg-primary rounded-[8px]" href="/">
            <img src={homeIcon} alt="home icon" />
            Go Home
          </a>
        </div>

      </section>
    </section>
  );
}

export default ErrorPage;
