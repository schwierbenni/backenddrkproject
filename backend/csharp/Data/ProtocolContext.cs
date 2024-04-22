using Models;
using Microsoft.EntityFrameworkCore;

namespace Data
{
    public class ProtocolContext : DbContext
    {
        public ProtocolContext(DbContextOptions<ProtocolContext> options) : base(options)
        {
        }

        public DbSet<Organization> Organizations { get; set; }

        public DbSet<User> Users  { get; set; }

        public DbSet<Protocol> Protocols { get; set; }

        public DbSet<AdditionalUser> AdditionalUsers { get; set; }

        protected override void OnModelCreating(ModelBuilder modelBuilder)
        {
            modelBuilder.Entity<Organization>()
                .HasMany(e => e.Users)
                .WithOne(e => e.Organization)
                .HasForeignKey(e => e.organizationId)
                .HasPrincipalKey(e => e.Id);

            modelBuilder.Entity<User>()
                .HasMany(e => e.Protocols)
                .WithOne(e => e.User)
                .HasForeignKey(e => e.userId)
                .HasPrincipalKey(e => e.Id);

            modelBuilder.Entity<AdditionalUser>()
                .HasKey(au => new { au.userId, au.protocolId });

            modelBuilder.Entity<AdditionalUser>()
                .HasOne(e => e.User)
                .WithMany(e => e.AdditionalUser)
                .HasForeignKey(e => e.userId);

            modelBuilder.Entity<AdditionalUser>()
                .HasOne(e => e.Protocol)
                .WithMany(e => e.AdditionalUser)
                .HasForeignKey(e => e.protocolId);
        }

        protected override void OnConfiguring(DbContextOptionsBuilder optionsBuilder)
        {
            var builder = WebApplication.CreateBuilder();
            var configuration = builder.Configuration;
            optionsBuilder.UseNpgsql(configuration.GetConnectionString("TestConnection"));
        }
    }
}
