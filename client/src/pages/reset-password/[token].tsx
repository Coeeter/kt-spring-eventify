import { GetServerSideProps } from 'next';
import Head from 'next/head';
import { useRouter } from 'next/router';
import { useEffect, useState } from 'react';
import { useForm } from 'react-hook-form';

type ResetPasswordProps = {
  expiresAt: Date;
  resetAt: Date | null;
  disabled: boolean;
};

type ResetPasswordForm = {
  password: string;
  confirmPassword: string;
};

export default function ResetPassword({
  disabled,
  expiresAt,
  resetAt,
}: ResetPasswordProps) {
  const token = useRouter().query.token as string;
  const [isComplete, setIsComplete] = useState(false);
  const [isDisabled, setIsDisabled] = useState(
    disabled || resetAt != null || expiresAt < new Date()
  );

  const {
    register,
    handleSubmit,
    formState: { errors },
    getValues,
  } = useForm<ResetPasswordForm>();

  const onSubmit = handleSubmit(async ({ password }) => {
    const res = await fetch(`http://localhost:8080/api/auth/reset-password`, {
      method: 'PUT',
      headers: {
        'Content-Type': 'application/json',
      },
      body: JSON.stringify({
        token,
        password,
      }),
    });
    if (res.status == 200) setIsComplete(true);
  });

  useEffect(() => {
    if (isDisabled) return;
    const timeout = setTimeout(() => {
      setIsDisabled(true);
    }, Date.parse(expiresAt.toString()) - new Date().getTime());
    return () => clearTimeout(timeout);
  }, [expiresAt]);

  return (
    <>
      <Head>
        <title>Reset Password</title>
      </Head>
      <div className="container mx-auto p-8 flex flex-col gap-8 items-center">
        <h2 className="text-center text-2xl font-bold text-slate-300">
          Reset Password
        </h2>
        <div className="bg-slate-800 shadow-md max-w-lg w-full p-3 rounded-md">
          <p className="text-lg text-center">
            {isDisabled
              ? 'This link is invalid or has expired.'
              : isComplete
              ? 'Your password has been reset.'
              : 'Enter your new password below.'}
          </p>
          {isDisabled || isComplete ? null : (
            <form onSubmit={onSubmit} className="flex flex-col gap-3 pt-3">
              <div className="w-full">
                <input
                  {...register('password', {
                    required: 'Password is required.',
                    minLength: {
                      value: 8,
                      message: 'Password must be at least 8 characters.',
                    },
                  })}
                  type="password"
                  placeholder="New Password"
                  className="bg-slate-800 w-full text-slate-300 border border-slate-700 rounded-md p-2 outline-none focus:border-blue-700 active:border-blue-700 placeholder:text-slate-500"
                />
                {errors.password && (
                  <p className="text-red-500 text-sm mt-1">
                    {errors.password.message}
                  </p>
                )}
              </div>
              <div className="w-full">
                <input
                  {...register('confirmPassword', {
                    required: 'Confirm password is required.',
                    validate: value =>
                      value === getValues().password ||
                      'The passwords do not match.',
                  })}
                  type="password"
                  name="confirmPassword"
                  placeholder="Confirm Password"
                  className="bg-slate-800 w-full text-slate-300 border border-slate-700 rounded-md p-2 outline-none focus:border-blue-700 active:border-blue-700 placeholder:text-slate-500"
                />
                {errors.confirmPassword && (
                  <p className="text-red-500 text-sm mt-1">
                    {errors.confirmPassword.message}
                  </p>
                )}
              </div>
              <button
                type="submit"
                className="bg-blue-700 transition duration-150 ease-in-out text-slate-300 rounded-md p-2 hover:bg-blue-800 active:bg-blue-900"
              >
                Reset Password
              </button>
            </form>
          )}
        </div>
      </div>
    </>
  );
}

export const getServerSideProps: GetServerSideProps = async context => {
  console.log('hello world');
  const { token } = context.query;
  const resetPasswordToken: ResetPasswordProps | null = await fetch(
    `http://localhost:8080/api/auth/token/${token}`
  ).then(res => {
    if (res.status != 200) return null;
    return res.json();
  });
  console.log(token);
  return {
    props: resetPasswordToken ?? { disabled: true },
  };
};
