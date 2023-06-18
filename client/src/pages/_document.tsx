import { Head, Html, Main, NextScript } from 'next/document'

export default function Document() {
  return (
    <Html lang="en" className='overflow-x-hidden relative scroll-smooth'>
      <Head />
      <body className='bg-slate-900 text-slate-400 overflow-x-hidden relative'>
        <Main />
        <NextScript />
      </body>
    </Html>
  )
}
